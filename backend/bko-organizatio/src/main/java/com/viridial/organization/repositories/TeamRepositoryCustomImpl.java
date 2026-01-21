package com.viridial.organization.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.organization.entities.TeamEntity;
import com.viridial.organization.forms.TeamSearchForm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom repository implementation for TeamEntity.
 */
@Repository
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Page<TeamEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TeamEntity> countRoot = countQuery.from(TeamEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<TeamEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildTeamSearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<TeamEntity> dataQuery = cb.createQuery(TeamEntity.class);
        Root<TeamEntity> dataRoot = dataQuery.from(TeamEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<TeamEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildTeamSearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "name");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<TeamEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<TeamEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildTeamSearchPredicates(CriteriaBuilder cb, Root<TeamEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof TeamSearchForm) {
            TeamSearchForm formSearch = (TeamSearchForm) searchForm;
            
            if (formSearch.getInternalCode() != null && !formSearch.getInternalCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("internalCode")), 
                    "%" + formSearch.getInternalCode().toLowerCase() + "%"));
            }
            if (formSearch.getName() != null && !formSearch.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + formSearch.getName().toLowerCase() + "%"));
            }
            if (formSearch.getEmail() != null && !formSearch.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), 
                    "%" + formSearch.getEmail().toLowerCase() + "%"));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getTeamTypeId() != null) {
                predicates.add(cb.equal(root.get("teamTypeId"), formSearch.getTeamTypeId()));
            }
            if (formSearch.getIndustryId() != null) {
                predicates.add(cb.equal(root.get("industryId"), formSearch.getIndustryId()));
            }
            if (formSearch.getParentId() != null) {
                predicates.add(cb.equal(root.get("parentId"), formSearch.getParentId()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<TeamEntity> findAllById(List<Long> ids) {
        return teamRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<TeamEntity> entities) {
        teamRepository.saveAllAndFlush(entities);
    }
}


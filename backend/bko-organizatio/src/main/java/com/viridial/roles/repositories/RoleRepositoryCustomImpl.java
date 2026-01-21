package com.viridial.roles.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.roles.entities.RoleEntity;
import com.viridial.roles.forms.RoleSearchForm;
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
 * Custom repository implementation for RoleEntity.
 */
@Repository
public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<RoleEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<RoleEntity> countRoot = countQuery.from(RoleEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<RoleEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildRoleSearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<RoleEntity> dataQuery = cb.createQuery(RoleEntity.class);
        Root<RoleEntity> dataRoot = dataQuery.from(RoleEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<RoleEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildRoleSearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "code");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<RoleEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<RoleEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildRoleSearchPredicates(CriteriaBuilder cb, Root<RoleEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof RoleSearchForm) {
            RoleSearchForm formSearch = (RoleSearchForm) searchForm;
            
            if (formSearch.getCode() != null && !formSearch.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), 
                    "%" + formSearch.getCode().toLowerCase() + "%"));
            }
            if (formSearch.getLabel() != null && !formSearch.getLabel().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("label")), 
                    "%" + formSearch.getLabel().toLowerCase() + "%"));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getAdmin() != null) {
                predicates.add(cb.equal(root.get("admin"), formSearch.getAdmin()));
            }
            if (formSearch.getParentId() != null) {
                predicates.add(cb.equal(root.get("parentId"), formSearch.getParentId()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<RoleEntity> findAllById(List<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<RoleEntity> entities) {
        roleRepository.saveAllAndFlush(entities);
    }
}


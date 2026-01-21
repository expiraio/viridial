package com.viridial.common.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.countries.entities.TimezoneEntity;
import com.viridial.countries.forms.TimezoneSearchForm;
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
 * Custom repository implementation for TimezoneEntity.
 * Provides additional methods for TimezoneEntity operations.
 */
@Repository
public class TimezoneRepositoryCustomImpl implements TimezoneRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TimezoneRepository timezoneRepository;

    @Override
    public Page<TimezoneEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TimezoneEntity> countRoot = countQuery.from(TimezoneEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<TimezoneEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildTimezoneSearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<TimezoneEntity> dataQuery = cb.createQuery(TimezoneEntity.class);
        Root<TimezoneEntity> dataRoot = dataQuery.from(TimezoneEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<TimezoneEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildTimezoneSearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "code");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<TimezoneEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<TimezoneEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildTimezoneSearchPredicates(CriteriaBuilder cb, Root<TimezoneEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof TimezoneSearchForm) {
            TimezoneSearchForm formSearch = (TimezoneSearchForm) searchForm;
            
            if (formSearch.getCode() != null && !formSearch.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), 
                    "%" + formSearch.getCode().toLowerCase() + "%"));
            }
            if (formSearch.getName() != null && !formSearch.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + formSearch.getName().toLowerCase() + "%"));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getUsesDst() != null) {
                predicates.add(cb.equal(root.get("usesDst"), formSearch.getUsesDst()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<TimezoneEntity> findAllById(List<Long> ids) {
        return timezoneRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<TimezoneEntity> entities) {
        timezoneRepository.saveAllAndFlush(entities);
    }
}


package com.viridial.referentiel.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.forms.FormSearch;
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
 * Custom repository implementation for ReferentialEntity.
 * Provides additional methods for logical delete operations and filtering.
 */
@Repository
public class ReferentialRepositoryCustomImpl implements ReferentialRepositoryCustom  {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReferentialRepository referentialRepository;

    public Page<ReferentialEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ReferentialEntity> countRoot = countQuery.from(ReferentialEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<ReferentialEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildFormSearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        // Data query
        CriteriaQuery<ReferentialEntity> dataQuery = cb.createQuery(ReferentialEntity.class);
        Root<ReferentialEntity> dataRoot = dataQuery.from(ReferentialEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<ReferentialEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildFormSearchPredicates(builder, root, form)));
        
        // Apply sorting
        List<Order> orders = dataBuilder.buildOrders(searchForm, "displayOrder");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        // Execute query with pagination
        TypedQuery<ReferentialEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ReferentialEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    /**
     * Builds predicates for FormSearch specific fields.
     * This method handles entity-specific search fields.
     */
    private List<Predicate> buildFormSearchPredicates(CriteriaBuilder cb, Root<ReferentialEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        // Handle FormSearch specific fields if it's a FormSearch instance
        if (searchForm instanceof FormSearch) {
            FormSearch formSearch = (FormSearch) searchForm;
            
            if (formSearch.getCode() != null && !formSearch.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), 
                    "%" + formSearch.getCode().toLowerCase() + "%"));
            }
            
            if (formSearch.getDataType() != null && !formSearch.getDataType().isEmpty()) {
                predicates.add(cb.equal(root.get("dataType"), formSearch.getDataType()));
            }
            
            if (formSearch.getLabel() != null && !formSearch.getLabel().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("label")), 
                    "%" + formSearch.getLabel().toLowerCase() + "%"));
            }
            
            if (formSearch.getTypeId() != null) {
                predicates.add(cb.equal(root.get("typeId"), formSearch.getTypeId()));
            }
            
            if (formSearch.getSubTypeId() != null) {
                predicates.add(cb.equal(root.get("subTypeId"), formSearch.getSubTypeId()));
            }
            
            if (formSearch.getParentId() != null) {
                predicates.add(cb.equal(root.get("parentId"), formSearch.getParentId()));
            }
            
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            
            if (formSearch.getLocale() != null && !formSearch.getLocale().isEmpty()) {
                predicates.add(cb.equal(root.get("locale"), formSearch.getLocale()));
            }
        }
        
        return predicates;
    }

    public List<ReferentialEntity> findAllById(List<Long> ids) {
       return referentialRepository.findAllById(ids);
    }

    public void saveAll(List<ReferentialEntity> entities) {
        referentialRepository.saveAllAndFlush(entities);
    }

    
}

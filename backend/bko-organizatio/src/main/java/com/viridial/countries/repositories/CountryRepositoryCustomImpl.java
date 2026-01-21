package com.viridial.countries.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.countries.entities.CountryEntity;
import com.viridial.countries.forms.CountrySearchForm;
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
 * Custom repository implementation for CountryEntity.
 */
@Repository
public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Page<CountryEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CountryEntity> countRoot = countQuery.from(CountryEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<CountryEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildCountrySearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<CountryEntity> dataQuery = cb.createQuery(CountryEntity.class);
        Root<CountryEntity> dataRoot = dataQuery.from(CountryEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<CountryEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildCountrySearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "name");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<CountryEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<CountryEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildCountrySearchPredicates(CriteriaBuilder cb, Root<CountryEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof CountrySearchForm) {
            CountrySearchForm formSearch = (CountrySearchForm) searchForm;
            
            if (formSearch.getName() != null && !formSearch.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + formSearch.getName().toLowerCase() + "%"));
            }
            if (formSearch.getIso2() != null && !formSearch.getIso2().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("iso2")), formSearch.getIso2().toLowerCase()));
            }
            if (formSearch.getIso3() != null && !formSearch.getIso3().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("iso3")), formSearch.getIso3().toLowerCase()));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getEnabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), formSearch.getEnabled()));
            }
            if (formSearch.getRegionId() != null) {
                predicates.add(cb.equal(root.get("regionId"), formSearch.getRegionId()));
            }
            if (formSearch.getSubRegionId() != null) {
                predicates.add(cb.equal(root.get("subRegionId"), formSearch.getSubRegionId()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<CountryEntity> findAllById(List<Long> ids) {
        return countryRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<CountryEntity> entities) {
        countryRepository.saveAllAndFlush(entities);
    }
}


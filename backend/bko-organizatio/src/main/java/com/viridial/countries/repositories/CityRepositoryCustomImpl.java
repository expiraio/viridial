package com.viridial.countries.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.countries.entities.CityEntity;
import com.viridial.countries.forms.CitySearchForm;
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
 * Custom repository implementation for CityEntity.
 */
@Repository
public class CityRepositoryCustomImpl implements CityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Page<CityEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CityEntity> countRoot = countQuery.from(CityEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<CityEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildCitySearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<CityEntity> dataQuery = cb.createQuery(CityEntity.class);
        Root<CityEntity> dataRoot = dataQuery.from(CityEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<CityEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildCitySearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "name");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<CityEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<CityEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildCitySearchPredicates(CriteriaBuilder cb, Root<CityEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof CitySearchForm) {
            CitySearchForm formSearch = (CitySearchForm) searchForm;
            
            if (formSearch.getName() != null && !formSearch.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + formSearch.getName().toLowerCase() + "%"));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getCapital() != null) {
                predicates.add(cb.equal(root.get("capital"), formSearch.getCapital()));
            }
            if (formSearch.getCountryId() != null) {
                predicates.add(cb.equal(root.get("countryId"), formSearch.getCountryId()));
            }
            if (formSearch.getTimezoneId() != null) {
                predicates.add(cb.equal(root.get("timezoneId"), formSearch.getTimezoneId()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<CityEntity> findAllById(List<Long> ids) {
        return cityRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<CityEntity> entities) {
        cityRepository.saveAllAndFlush(entities);
    }
}

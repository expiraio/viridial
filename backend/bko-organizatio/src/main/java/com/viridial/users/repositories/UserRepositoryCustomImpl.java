package com.viridial.users.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.users.entities.UserEntity;
import com.viridial.users.forms.UserSearchForm;
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
 * Custom repository implementation for UserEntity.
 */
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserEntity> search(SearchForm searchForm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UserEntity> countRoot = countQuery.from(UserEntity.class);
        countQuery.select(cb.count(countRoot));
        
        SearchCriteriaBuilder<UserEntity> countBuilder = SearchCriteriaBuilder.of(cb, countRoot);
        countQuery.where(countBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildUserSearchPredicates(builder, root, form)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        CriteriaQuery<UserEntity> dataQuery = cb.createQuery(UserEntity.class);
        Root<UserEntity> dataRoot = dataQuery.from(UserEntity.class);
        dataQuery.select(dataRoot);
        
        SearchCriteriaBuilder<UserEntity> dataBuilder = SearchCriteriaBuilder.of(cb, dataRoot);
        dataQuery.where(dataBuilder.buildPredicates(searchForm, 
            (builder, root, form) -> buildUserSearchPredicates(builder, root, form)));
        
        List<Order> orders = dataBuilder.buildOrders(searchForm, "createdAt");
        if (!orders.isEmpty()) {
            dataQuery.orderBy(orders);
        }
        
        TypedQuery<UserEntity> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<UserEntity> results = typedQuery.getResultList();
        
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildUserSearchPredicates(CriteriaBuilder cb, Root<UserEntity> root, SearchForm searchForm) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (searchForm instanceof UserSearchForm) {
            UserSearchForm formSearch = (UserSearchForm) searchForm;
            
            if (formSearch.getEmail() != null && !formSearch.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), 
                    "%" + formSearch.getEmail().toLowerCase() + "%"));
            }
            if (formSearch.getFirstName() != null && !formSearch.getFirstName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), 
                    "%" + formSearch.getFirstName().toLowerCase() + "%"));
            }
            if (formSearch.getLastName() != null && !formSearch.getLastName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), 
                    "%" + formSearch.getLastName().toLowerCase() + "%"));
            }
            if (formSearch.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), formSearch.getActive()));
            }
            if (formSearch.getStatusId() != null) {
                predicates.add(cb.equal(root.get("statusId"), formSearch.getStatusId()));
            }
        }
        
        return predicates;
    }

    @Override
    public List<UserEntity> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<UserEntity> entities) {
        userRepository.saveAllAndFlush(entities);
    }
}


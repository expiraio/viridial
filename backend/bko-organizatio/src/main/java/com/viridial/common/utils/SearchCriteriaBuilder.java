package com.viridial.common.utils;

import com.viridial.common.entities.BaseEntity;
import com.viridial.common.form.SearchForm;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building JPA Criteria API predicates, filters, and orders
 * from SearchForm. Can be reused across all repositories.
 * 
 * @param <T> The entity type that extends BaseEntity
 */
public class SearchCriteriaBuilder<T extends BaseEntity> {

    private final CriteriaBuilder cb;
    private final Root<T> root;

    public SearchCriteriaBuilder(CriteriaBuilder cb, Root<T> root) {
        this.cb = cb;
        this.root = root;
    }

    /**
     * Functional interface for handling custom field predicates.
     */
    @FunctionalInterface
    public interface CustomFieldHandler<T extends BaseEntity> {
        List<Predicate> apply(CriteriaBuilder cb, Root<T> root, SearchForm searchForm);
    }

    /**
     * Builds predicates from search form criteria.
     * 
     * @param searchForm the search form with filters, sorts, and pagination
     * @param customFieldHandler optional handler for entity-specific fields (receives cb, root, and searchForm)
     * @return the combined predicate
     */
    public Predicate buildPredicates(SearchForm searchForm, 
                                     CustomFieldHandler<T> customFieldHandler) {
        List<Predicate> predicates = new ArrayList<>();
        
        // Exclude deleted records by default
        if (!searchForm.getIncludeDeleted()) {
            predicates.add(cb.isNull(root.get("deletedAt")));
        }
        
        // Handle ID filters
        if (searchForm.getId() != null) {
            predicates.add(cb.equal(root.get("id"), searchForm.getId()));
        }
        
        if (searchForm.getIds() != null && !searchForm.getIds().isEmpty()) {
            predicates.add(root.get("id").in(searchForm.getIds()));
        }
        
        // Handle custom field handlers (for entity-specific fields)
        if (customFieldHandler != null) {
            List<Predicate> customPredicates = customFieldHandler.apply(cb, root, searchForm);
            if (customPredicates != null && !customPredicates.isEmpty()) {
                predicates.addAll(customPredicates);
            }
        }
        
        // Handle generic filters
        if (searchForm.getFilters() != null && !searchForm.getFilters().isEmpty()) {
            List<Predicate> filterPredicates = new ArrayList<>();
            for (SearchForm.FilterCriteria filter : searchForm.getFilters()) {
                Predicate filterPredicate = buildFilterPredicate(filter);
                if (filterPredicate != null) {
                    filterPredicates.add(filterPredicate);
                }
            }
            
            if (!filterPredicates.isEmpty()) {
                if ("OR".equalsIgnoreCase(searchForm.getLogicalOperator())) {
                    predicates.add(cb.or(filterPredicates.toArray(new Predicate[0])));
                } else {
                    predicates.add(cb.and(filterPredicates.toArray(new Predicate[0])));
                }
            }
        }
        
        // Handle range filters
        if (searchForm.getRanges() != null && !searchForm.getRanges().isEmpty()) {
            for (var entry : searchForm.getRanges().entrySet()) {
                String field = entry.getKey();
                SearchForm.RangeValue range = entry.getValue();
                
                try {
                    Path<?> fieldPath = root.get(field);
                    
                    if (range.getMin() != null && range.getMax() != null) {
                        predicates.add(cb.between((Expression<Comparable>) fieldPath, 
                            (Comparable) range.getMin(), (Comparable) range.getMax()));
                    } else if (range.getMin() != null) {
                        predicates.add(cb.greaterThanOrEqualTo((Expression<Comparable>) fieldPath, 
                            (Comparable) range.getMin()));
                    } else if (range.getMax() != null) {
                        predicates.add(cb.lessThanOrEqualTo((Expression<Comparable>) fieldPath, 
                            (Comparable) range.getMax()));
                    }
                } catch (IllegalArgumentException e) {
                    // Field doesn't exist, skip this range filter
                }
            }
        }
        
        return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Builds a predicate for a single filter criteria.
     * 
     * @param filter the filter criteria
     * @return the predicate or null if filter is invalid
     */
    public Predicate buildFilterPredicate(SearchForm.FilterCriteria filter) {
        String field = filter.getField();
        SearchForm.FilterOperator operator = filter.getOperator();
        Object value = filter.getValue();
        
        try {
            Path<?> fieldPath = root.get(field);
            
            switch (operator) {
                case EQUALS:
                    return cb.equal(fieldPath, value);
                case NOT_EQUALS:
                    return cb.notEqual(fieldPath, value);
                case CONTAINS:
                    if (value instanceof String) {
                        return cb.like(cb.lower((Expression<String>) fieldPath), 
                            "%" + ((String) value).toLowerCase() + "%");
                    }
                    break;
                case NOT_CONTAINS:
                    if (value instanceof String) {
                        return cb.notLike(cb.lower((Expression<String>) fieldPath), 
                            "%" + ((String) value).toLowerCase() + "%");
                    }
                    break;
                case STARTS_WITH:
                    if (value instanceof String) {
                        return cb.like(cb.lower((Expression<String>) fieldPath), 
                            ((String) value).toLowerCase() + "%");
                    }
                    break;
                case ENDS_WITH:
                    if (value instanceof String) {
                        return cb.like(cb.lower((Expression<String>) fieldPath), 
                            "%" + ((String) value).toLowerCase());
                    }
                    break;
                case GREATER_THAN:
                    return cb.greaterThan((Expression<Comparable>) fieldPath, (Comparable) value);
                case GREATER_THAN_OR_EQUAL:
                    return cb.greaterThanOrEqualTo((Expression<Comparable>) fieldPath, (Comparable) value);
                case LESS_THAN:
                    return cb.lessThan((Expression<Comparable>) fieldPath, (Comparable) value);
                case LESS_THAN_OR_EQUAL:
                    return cb.lessThanOrEqualTo((Expression<Comparable>) fieldPath, (Comparable) value);
                case IN:
                    if (value instanceof List) {
                        return fieldPath.in((List<?>) value);
                    }
                    break;
                case NOT_IN:
                    if (value instanceof List) {
                        return cb.not(fieldPath.in((List<?>) value));
                    }
                    break;
                case IS_NULL:
                    return cb.isNull(fieldPath);
                case IS_NOT_NULL:
                    return cb.isNotNull(fieldPath);
                case IS_TRUE:
                    return cb.isTrue((Expression<Boolean>) fieldPath);
                case IS_FALSE:
                    return cb.isFalse((Expression<Boolean>) fieldPath);
                case BETWEEN:
                    if (value instanceof List && ((List<?>) value).size() == 2) {
                        List<?> values = (List<?>) value;
                        return cb.between((Expression<Comparable>) fieldPath, 
                            (Comparable) values.get(0), (Comparable) values.get(1));
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            // Field doesn't exist, return null
            return null;
        }
        
        return null;
    }

    /**
     * Builds order list from search form sort criteria.
     * 
     * @param searchForm the search form with sort criteria
     * @param defaultSortField the default field to sort by if no sort is specified
     * @return list of order expressions
     */
    public List<Order> buildOrders(SearchForm searchForm, String defaultSortField) {
        List<Order> orders = new ArrayList<>();
        
        // Use dynamic sorts if available
        if (searchForm.getSorts() != null && !searchForm.getSorts().isEmpty()) {
            for (SearchForm.SortCriteria sort : searchForm.getSorts()) {
                if (sort.getField() != null && !sort.getField().isEmpty()) {
                    try {
                        Path<?> fieldPath = root.get(sort.getField());
                        if ("DESC".equalsIgnoreCase(sort.getDirection())) {
                            orders.add(cb.desc((Expression<? extends Comparable>) fieldPath));
                        } else {
                            orders.add(cb.asc((Expression<? extends Comparable>) fieldPath));
                        }
                    } catch (IllegalArgumentException e) {
                        // Field doesn't exist, skip this sort
                    }
                }
            }
        } else if (searchForm.getSortBy() != null && !searchForm.getSortBy().isEmpty()) {
            // Fallback to single sort
            try {
                Path<?> fieldPath = root.get(searchForm.getSortBy());
                if ("DESC".equalsIgnoreCase(searchForm.getSortDirection())) {
                    orders.add(cb.desc((Expression<? extends Comparable>) fieldPath));
                } else {
                    orders.add(cb.asc((Expression<? extends Comparable>) fieldPath));
                }
            } catch (IllegalArgumentException e) {
                // Field doesn't exist, use default
            }
        }
        
        // Apply default sort if no orders were added
        if (orders.isEmpty() && defaultSortField != null && !defaultSortField.isEmpty()) {
            try {
                Path<?> fieldPath = root.get(defaultSortField);
                orders.add(cb.asc((Expression<? extends Comparable>) fieldPath));
            } catch (IllegalArgumentException e) {
                // Default field doesn't exist, return empty list
            }
        }
        
        return orders;
    }

    /**
     * Static factory method to create a new SearchCriteriaBuilder instance.
     * 
     * @param cb the criteria builder
     * @param root the root entity path
     * @param <T> the entity type
     * @return a new SearchCriteriaBuilder instance
     */
    public static <T extends BaseEntity> SearchCriteriaBuilder<T> of(CriteriaBuilder cb, Root<T> root) {
        return new SearchCriteriaBuilder<>(cb, root);
    }
}


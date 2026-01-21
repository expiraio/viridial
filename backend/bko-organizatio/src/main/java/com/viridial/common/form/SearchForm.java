package com.viridial.common.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dynamic search form that supports flexible filtering, sorting, and pagination.
 * Can be used for any entity type without modification.
 */
public class SearchForm {
    
    private Long id;
    private List<Long> ids;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    
    /**
     * List of sort criteria for multi-column sorting.
     * Format: [{"field": "name", "direction": "ASC"}, ...]
     */
    private List<SortCriteria> sorts;
    
    /**
     * Generic filters with field name, operator, and value.
     * Format: [{"field": "name", "operator": "CONTAINS", "value": "test"}, ...]
     */
    private List<FilterCriteria> filters;
    
    /**
     * Range filters for numeric or date fields.
     * Format: {"field": {"min": 10, "max": 100}}
     */
    private Map<String, RangeValue> ranges;
    
    /**
     * Logical operator for combining filters: AND or OR (default: AND)
     */
    private String logicalOperator = "AND";
    
    /**
     * Include deleted records (default: false)
     */
    private Boolean includeDeleted = false;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size != null && size > 0 ? size : 20;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection != null ? sortDirection.toUpperCase() : "ASC";
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public List<SortCriteria> getSorts() {
        if (sorts == null) {
            sorts = new ArrayList<>();
            // Add default sort if sortBy is provided
            if (sortBy != null && !sortBy.isEmpty()) {
                sorts.add(new SortCriteria(sortBy, getSortDirection()));
            }
        }
        return sorts;
    }

    public void setSorts(List<SortCriteria> sorts) {
        this.sorts = sorts;
    }

    public List<FilterCriteria> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        return filters;
    }

    public void setFilters(List<FilterCriteria> filters) {
        this.filters = filters;
    }

    public Map<String, RangeValue> getRanges() {
        if (ranges == null) {
            ranges = new HashMap<>();
        }
        return ranges;
    }

    public void setRanges(Map<String, RangeValue> ranges) {
        this.ranges = ranges;
    }

    public String getLogicalOperator() {
        return logicalOperator != null ? logicalOperator.toUpperCase() : "AND";
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Boolean getIncludeDeleted() {
        return includeDeleted != null ? includeDeleted : false;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    /**
     * Adds a filter criteria.
     */
    public SearchForm addFilter(String field, FilterOperator operator, Object value) {
        getFilters().add(new FilterCriteria(field, operator, value));
        return this;
    }

    /**
     * Adds a sort criteria.
     */
    public SearchForm addSort(String field, String direction) {
        getSorts().add(new SortCriteria(field, direction));
        return this;
    }

    /**
     * Adds a range filter.
     */
    public SearchForm addRange(String field, Object min, Object max) {
        getRanges().put(field, new RangeValue(min, max));
        return this;
    }

    /**
     * Filter criteria with operator and value.
     */
    public static class FilterCriteria {
        private String field;
        private FilterOperator operator;
        private Object value;

        public FilterCriteria() {
        }

        public FilterCriteria(String field, FilterOperator operator, Object value) {
            this.field = field;
            this.operator = operator;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public FilterOperator getOperator() {
            return operator;
        }

        public void setOperator(FilterOperator operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    /**
     * Sort criteria with field and direction.
     */
    public static class SortCriteria {
        private String field;
        private String direction;

        public SortCriteria() {
        }

        public SortCriteria(String field, String direction) {
            this.field = field;
            this.direction = direction != null ? direction.toUpperCase() : "ASC";
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getDirection() {
            return direction != null ? direction.toUpperCase() : "ASC";
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }

    /**
     * Range value for min/max queries.
     */
    public static class RangeValue {
        private Object min;
        private Object max;

        public RangeValue() {
        }

        public RangeValue(Object min, Object max) {
            this.min = min;
            this.max = max;
        }

        public Object getMin() {
            return min;
        }

        public void setMin(Object min) {
            this.min = min;
        }

        public Object getMax() {
            return max;
        }

        public void setMax(Object max) {
            this.max = max;
        }
    }

    /**
     * Filter operators for dynamic queries.
     */
    public enum FilterOperator {
        EQUALS,              // field = value
        NOT_EQUALS,          // field != value
        CONTAINS,            // field LIKE %value%
        NOT_CONTAINS,        // field NOT LIKE %value%
        STARTS_WITH,         // field LIKE value%
        ENDS_WITH,           // field LIKE %value
        GREATER_THAN,        // field > value
        GREATER_THAN_OR_EQUAL, // field >= value
        LESS_THAN,           // field < value
        LESS_THAN_OR_EQUAL,  // field <= value
        IN,                  // field IN (value1, value2, ...)
        NOT_IN,              // field NOT IN (value1, value2, ...)
        IS_NULL,             // field IS NULL
        IS_NOT_NULL,         // field IS NOT NULL
        BETWEEN,             // field BETWEEN value1 AND value2
        IS_TRUE,             // field = true
        IS_FALSE             // field = false
    }
}

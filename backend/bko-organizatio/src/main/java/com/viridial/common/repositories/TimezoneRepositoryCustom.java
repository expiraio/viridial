package com.viridial.common.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.countries.entities.TimezoneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for TimezoneEntity.
 */
public interface TimezoneRepositoryCustom {
    Page<TimezoneEntity> search(SearchForm searchForm, Pageable pageable);
    List<TimezoneEntity> findAllById(List<Long> ids);
    void saveAll(List<TimezoneEntity> entities);
}


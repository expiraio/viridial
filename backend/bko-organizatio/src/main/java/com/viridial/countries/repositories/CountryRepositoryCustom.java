package com.viridial.countries.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.countries.entities.CountryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for CountryEntity.
 */
public interface CountryRepositoryCustom {
    Page<CountryEntity> search(SearchForm searchForm, Pageable pageable);
    List<CountryEntity> findAllById(List<Long> ids);
    void saveAll(List<CountryEntity> entities);
}


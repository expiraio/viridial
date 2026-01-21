package com.viridial.countries.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.countries.entities.CityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for CityEntity.
 * Defines additional methods beyond standard JPA repository operations.
 */
public interface CityRepositoryCustom {
    Page<CityEntity> search(SearchForm searchForm, Pageable pageable);
    List<CityEntity> findAllById(List<Long> ids);
    void saveAll(List<CityEntity> entities);
}


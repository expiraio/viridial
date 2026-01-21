package com.viridial.organization.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.organization.entities.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for TeamEntity.
 * Defines additional methods beyond standard JPA repository operations.
 */
public interface TeamRepositoryCustom {
    Page<TeamEntity> search(SearchForm searchForm, Pageable pageable);
    List<TeamEntity> findAllById(List<Long> ids);
    void saveAll(List<TeamEntity> entities);
}


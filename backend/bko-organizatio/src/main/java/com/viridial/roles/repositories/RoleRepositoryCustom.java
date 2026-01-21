package com.viridial.roles.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.roles.entities.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for RoleEntity.
 * Defines additional methods beyond standard JPA repository operations.
 */
public interface RoleRepositoryCustom {
    Page<RoleEntity> search(SearchForm searchForm, Pageable pageable);
    List<RoleEntity> findAllById(List<Long> ids);
    void saveAll(List<RoleEntity> entities);
}


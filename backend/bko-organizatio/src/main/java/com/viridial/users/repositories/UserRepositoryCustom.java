package com.viridial.users.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.users.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom repository interface for UserEntity.
 * Defines additional methods beyond standard JPA repository operations.
 */
public interface UserRepositoryCustom {
    Page<UserEntity> search(SearchForm searchForm, Pageable pageable);
    List<UserEntity> findAllById(List<Long> ids);
    void saveAll(List<UserEntity> entities);
}


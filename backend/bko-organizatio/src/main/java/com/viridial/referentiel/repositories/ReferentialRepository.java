package com.viridial.referentiel.repositories;

import com.viridial.referentiel.entities.ReferentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for ReferentialEntity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface ReferentialRepository extends JpaRepository<ReferentialEntity, Long>, 
        JpaSpecificationExecutor<ReferentialEntity> {

    java.util.Optional<ReferentialEntity> findByCodeAndDataType(String code, String dataType);
}


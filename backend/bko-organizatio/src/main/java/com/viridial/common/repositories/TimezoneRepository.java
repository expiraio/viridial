package com.viridial.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.viridial.countries.entities.TimezoneEntity;

/**
 * Repository for TimezoneEntity.
 */
@Repository
public interface TimezoneRepository extends JpaRepository<TimezoneEntity, Long>, 
        JpaSpecificationExecutor<TimezoneEntity> {
    
    java.util.Optional<TimezoneEntity> findByCode(String code);
}


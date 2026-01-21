package com.viridial.feature.repositories;

import com.viridial.feature.entities.FeatureTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for FeatureTypeEntity.
 */
@Repository
public interface FeatureTypeRepository extends JpaRepository<FeatureTypeEntity, Long>, 
        JpaSpecificationExecutor<FeatureTypeEntity> {

}


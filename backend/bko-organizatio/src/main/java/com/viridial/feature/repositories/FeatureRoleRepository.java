package com.viridial.feature.repositories;

import com.viridial.feature.entities.FeatureRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for FeatureRoleEntity.
 */
@Repository
public interface FeatureRoleRepository extends JpaRepository<FeatureRoleEntity, Long>, 
        JpaSpecificationExecutor<FeatureRoleEntity> {

}


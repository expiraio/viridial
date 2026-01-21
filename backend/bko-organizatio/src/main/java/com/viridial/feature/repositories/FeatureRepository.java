package com.viridial.feature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viridial.feature.entities.FeatureEntity;

/**
 * Repository for TeamAddressEntity.
 */
@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {

}


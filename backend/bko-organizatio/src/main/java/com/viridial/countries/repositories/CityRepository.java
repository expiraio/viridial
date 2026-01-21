package com.viridial.countries.repositories;

import com.viridial.countries.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for CityEntity.
 */
@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long>, 
        JpaSpecificationExecutor<CityEntity> {

    java.util.Optional<CityEntity> findByNameAndCountryId(String name, Long countryId);
}


package com.viridial.countries.repositories;

import com.viridial.countries.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for CountryEntity.
 */
@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long>, 
        JpaSpecificationExecutor<CountryEntity> {

    java.util.Optional<CountryEntity> findByIso2(String iso2);
}


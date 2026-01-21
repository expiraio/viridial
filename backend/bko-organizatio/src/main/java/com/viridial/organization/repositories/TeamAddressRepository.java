package com.viridial.organization.repositories;

import com.viridial.organization.entities.TeamAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for TeamAddressEntity.
 */
@Repository
public interface TeamAddressRepository extends JpaRepository<TeamAddressEntity, Long> {

}


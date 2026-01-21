package com.viridial.organization.repositories;

import com.viridial.organization.entities.TeamPhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for TeamPhoneEntity.
 */
@Repository
public interface TeamPhoneRepository extends JpaRepository<TeamPhoneEntity, Long> {

}


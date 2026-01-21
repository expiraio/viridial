package com.viridial.organization.repositories;

import com.viridial.organization.entities.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Repository for TeamEntity.
 */
@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long>, 
        JpaSpecificationExecutor<TeamEntity> {

}


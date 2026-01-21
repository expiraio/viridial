package com.viridial.roles.repositories;

import com.viridial.roles.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for RoleEntity.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, 
        JpaSpecificationExecutor<RoleEntity> {

}


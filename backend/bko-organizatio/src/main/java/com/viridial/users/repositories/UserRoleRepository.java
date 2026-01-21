package com.viridial.users.repositories;

import com.viridial.users.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for UserRoleEntity.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>, 
        JpaSpecificationExecutor<UserRoleEntity> {

}


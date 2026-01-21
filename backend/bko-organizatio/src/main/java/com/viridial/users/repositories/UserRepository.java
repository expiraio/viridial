package com.viridial.users.repositories;

import com.viridial.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Standard JPA repository for UserEntity.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, 
        JpaSpecificationExecutor<UserEntity> {

}


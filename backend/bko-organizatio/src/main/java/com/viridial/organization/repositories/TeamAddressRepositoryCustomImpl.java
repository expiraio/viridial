package com.viridial.organization.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Custom repository implementation for TeamAddressEntity.
 */
@Repository
public class TeamAddressRepositoryCustomImpl implements TeamAddressRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TeamAddressRepository teamAddressRepository;
}


package com.viridial.organization.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Custom repository implementation for TeamPhoneEntity.
 */
@Repository
public class TeamPhoneRepositoryCustomImpl implements TeamPhoneRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TeamPhoneRepository teamPhoneRepository;
}


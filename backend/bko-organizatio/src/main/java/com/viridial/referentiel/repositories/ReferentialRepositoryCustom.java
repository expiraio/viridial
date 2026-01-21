package com.viridial.referentiel.repositories;

import com.viridial.common.form.SearchForm;
import com.viridial.common.utils.SearchCriteriaBuilder;
import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.forms.FormSearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom repository implementation for ReferentialEntity.
 * Provides additional methods for logical delete operations and filtering.
 */
@Repository
public interface ReferentialRepositoryCustom  {


    public Page<ReferentialEntity> search(SearchForm searchForm, Pageable pageable) ;

    

    public List<ReferentialEntity> findAllById(List<Long> ids);

    public void saveAll(List<ReferentialEntity> entities);

    
}

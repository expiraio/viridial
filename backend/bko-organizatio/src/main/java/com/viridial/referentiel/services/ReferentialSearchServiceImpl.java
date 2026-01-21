package com.viridial.referentiel.services;

import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.forms.FormSearch;
import com.viridial.referentiel.forms.ReferentialForm;
import com.viridial.referentiel.mapper.ReferentialMapper;
import com.viridial.referentiel.repositories.ReferentialRepositoryCustom;
import com.viridial.common.forms.PaginatedResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for ReferentialEntity operations.
 */
@Service
@Transactional
public class ReferentialSearchServiceImpl implements ReferentialSearchService {

    @Autowired
    private ReferentialRepositoryCustom referentialRepositoryCustom;
    
    
    
    @Autowired
    private ReferentialMapper referentialMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<ReferentialForm> search(FormSearch search) {
        // Build pageable from search form
        Pageable pageable = buildPageable(search);
        
        // Execute search
        Page<ReferentialEntity> page = referentialRepositoryCustom.search(search, pageable);
        
        // Convert entities to forms
        List<ReferentialForm> forms = page.getContent().stream()
                .map(referentialMapper::mapEntityToForm)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(forms, page.getTotalElements(), page.getNumber(), page.getSize());
    }

    /**
     * Builds Pageable from search form.
     */
    private Pageable buildPageable(FormSearch search) {
        int page = search.getPage();
        int size = search.getSize();
        
        // If sorts are provided, use them; otherwise use default
        if (search.getSorts() != null && !search.getSorts().isEmpty()) {
            org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(
                search.getSorts().stream()
                    .map(s -> new org.springframework.data.domain.Sort.Order(
                        "DESC".equalsIgnoreCase(s.getDirection()) 
                            ? org.springframework.data.domain.Sort.Direction.DESC 
                            : org.springframework.data.domain.Sort.Direction.ASC,
                        s.getField()
                    ))
                    .collect(Collectors.toList())
            );
            return PageRequest.of(page, size, sort);
        } else if (search.getSortBy() != null && !search.getSortBy().isEmpty()) {
            org.springframework.data.domain.Sort.Direction direction = 
                "DESC".equalsIgnoreCase(search.getSortDirection()) 
                    ? org.springframework.data.domain.Sort.Direction.DESC 
                    : org.springframework.data.domain.Sort.Direction.ASC;
            return PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, search.getSortBy()));
        } else {
            // Default sort by display order
            return PageRequest.of(page, size, 
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "displayOrder"));
        }
    }

}

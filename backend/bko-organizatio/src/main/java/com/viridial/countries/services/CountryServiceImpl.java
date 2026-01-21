package com.viridial.countries.services;

import com.viridial.countries.entities.CountryEntity;
import com.viridial.countries.forms.CountryForm;
import com.viridial.countries.forms.CountrySearchForm;
import com.viridial.countries.mapper.CountryMapper;
import com.viridial.countries.repositories.CountryRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for CountryEntity operations.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepositoryCustom countryRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<CountryForm> search(CountrySearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<CountryEntity> page = countryRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(CountryMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<CountryEntity> entities = countryRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        countryRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<CountryEntity> entities = countryRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(CountryEntity::delete);
        }
        countryRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(CountrySearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}


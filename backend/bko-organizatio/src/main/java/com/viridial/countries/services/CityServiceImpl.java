package com.viridial.countries.services;

import com.viridial.countries.entities.CityEntity;
import com.viridial.countries.forms.CityForm;
import com.viridial.countries.forms.CitySearchForm;
import com.viridial.countries.mapper.CityMapper;
import com.viridial.countries.repositories.CityRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for CityEntity operations.
 */
@Service
@Transactional
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepositoryCustom cityRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<CityForm> search(CitySearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<CityEntity> page = cityRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(CityMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<CityEntity> entities = cityRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        cityRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<CityEntity> entities = cityRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(CityEntity::delete);
        }
        cityRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(CitySearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}


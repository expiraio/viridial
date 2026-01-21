package com.viridial.countries.services;

import com.viridial.common.repositories.TimezoneRepositoryCustom;
import com.viridial.countries.entities.TimezoneEntity;
import com.viridial.countries.forms.TimezoneForm;
import com.viridial.countries.forms.TimezoneSearchForm;
import com.viridial.countries.mapper.TimezoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for TimezoneEntity operations.
 */
@Service
@Transactional
public class TimezoneServiceImpl implements TimezoneService {
    @Autowired
    private TimezoneRepositoryCustom timezoneRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<TimezoneForm> search(TimezoneSearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<TimezoneEntity> page = timezoneRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(TimezoneMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<TimezoneEntity> entities = timezoneRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        timezoneRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<TimezoneEntity> entities = timezoneRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(TimezoneEntity::delete);
        }
        timezoneRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(TimezoneSearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}


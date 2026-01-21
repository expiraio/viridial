package com.viridial.roles.services;

import com.viridial.roles.entities.RoleEntity;
import com.viridial.roles.forms.RoleForm;
import com.viridial.roles.forms.RoleSearchForm;
import com.viridial.roles.mapper.RoleMapper;
import com.viridial.roles.repositories.RoleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for RoleEntity operations.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepositoryCustom roleRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<RoleForm> search(RoleSearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<RoleEntity> page = roleRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(RoleMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<RoleEntity> entities = roleRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        roleRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<RoleEntity> entities = roleRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(RoleEntity::delete);
        }
        roleRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(RoleSearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}


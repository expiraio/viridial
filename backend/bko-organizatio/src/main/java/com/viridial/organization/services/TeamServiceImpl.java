package com.viridial.organization.services;

import com.viridial.organization.entities.TeamEntity;
import com.viridial.organization.forms.TeamForm;
import com.viridial.organization.forms.TeamSearchForm;
import com.viridial.organization.mapper.TeamMapper;
import com.viridial.organization.repositories.TeamRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for TeamEntity operations.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepositoryCustom teamRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<TeamForm> search(TeamSearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<TeamEntity> page = teamRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(TeamMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<TeamEntity> entities = teamRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        teamRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<TeamEntity> entities = teamRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(TeamEntity::delete);
        }
        teamRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(TeamSearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}


package com.viridial.referentiel.services;

import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.mapper.ReferentialMapper;
import com.viridial.referentiel.repositories.ReferentialRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Service implementation for ReferentialEntity operations.
 */
@Service
@Transactional
public class ReferentialUpdateServiceImpl implements ReferentialUpdateService {

    @Autowired
    private ReferentialRepositoryCustom referentialRepositoryCustom;
    
    

    @Transactional
    @Override
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        // Find all entities by IDs
        List<ReferentialEntity> entities = referentialRepositoryCustom.findAllById(ids);
        // Update active status
        entities.forEach(entity -> entity.setActive(active));
        // Save all updated entities
        referentialRepositoryCustom.saveAll(entities);
        
        return entities.size();
    }

    @Transactional
    @Override
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        // Find all entities by IDs
        List<ReferentialEntity> entities = referentialRepositoryCustom.findAllById(ids);
        // Perform logical delete
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(entity -> entity.delete());
        }
        // Save all deleted entities
        referentialRepositoryCustom.saveAll(entities);
        return entities.size();
    }
}

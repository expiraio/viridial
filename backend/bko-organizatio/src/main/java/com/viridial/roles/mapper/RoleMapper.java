package com.viridial.roles.mapper;

import com.viridial.roles.entities.RoleEntity;
import com.viridial.roles.forms.RoleForm;

/**
 * Mapper for converting between RoleEntity and RoleForm.
 */
public class RoleMapper {
    public static RoleForm mapEntityToForm(RoleEntity entity) {
        if (entity == null) return null;
        
        RoleForm form = new RoleForm();
        form.setId(entity.getId());
        form.setCode(entity.getCode());
        form.setLabel(entity.getLabel());
        form.setDescription(entity.getDescription());
        form.setActive(entity.isActive());
        form.setAdmin(entity.isAdmin());
        form.setParentId(entity.getParentId());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


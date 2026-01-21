package com.viridial.users.mapper;

import com.viridial.users.entities.UserEntity;
import com.viridial.users.forms.UserForm;

/**
 * Mapper for converting between UserEntity and UserForm.
 */
public class UserMapper {
    public static UserForm mapEntityToForm(UserEntity entity) {
        if (entity == null) return null;
        
        UserForm form = new UserForm();
        form.setId(entity.getId());
        form.setFirstName(entity.getFirstName());
        form.setLastName(entity.getLastName());
        form.setEmail(entity.getEmail());
        form.setStatusId(entity.getStatusId());
        form.setActive(entity.isActive());
        form.setLastLoginAt(entity.getLastLoginAt());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


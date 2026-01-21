package com.viridial.countries.mapper;

import com.viridial.countries.entities.TimezoneEntity;
import com.viridial.countries.forms.TimezoneForm;

/**
 * Mapper for converting between TimezoneEntity and TimezoneForm.
 */
public class TimezoneMapper {
    public static TimezoneForm mapEntityToForm(TimezoneEntity entity) {
        if (entity == null) return null;
        
        TimezoneForm form = new TimezoneForm();
        form.setId(entity.getId());
        form.setCode(entity.getCode());
        form.setName(entity.getName());
        form.setAbbreviation(entity.getAbbreviation());
        form.setUtcOffset(entity.getUtcOffset());
        form.setDstOffset(entity.getDstOffset());
        form.setUsesDst(entity.isUsesDst());
        form.setDescription(entity.getDescription());
        form.setActive(entity.isActive());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


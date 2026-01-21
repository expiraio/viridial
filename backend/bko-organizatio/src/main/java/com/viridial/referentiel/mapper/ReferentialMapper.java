package com.viridial.referentiel.mapper;

import org.springframework.stereotype.Component;

import com.viridial.common.mapper.ExtMapper;
import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.forms.ReferentialForm;

/**
 * Mapper for converting between ReferentialEntity and ReferentialForm.
 */
@Component
public class ReferentialMapper extends ExtMapper{
    
    /**
     * Maps entity to form.
     *
     * @param entity the entity to map
     * @return the mapped form
     */
    public ReferentialForm mapEntityToForm(ReferentialEntity entity) {
        if (entity == null) {
            return null;
        }
        
        ReferentialForm form = new ReferentialForm();
        toForm(entity, form);
        form.setId(entity.getId());
        form.setCode(entity.getCode());
        form.setDataType(entity.getDataType());
        form.setLabel(entity.getLabel());
        form.setDescription(entity.getDescription());
        form.setExternalCode(entity.getExternalCode());
        form.setIconUrl(entity.getIconUrl());
        form.setDisplayOrder(entity.getDisplayOrder());
        form.setLocale(entity.getLocale());
        form.setTypeId(entity.getTypeId());
        form.setSubTypeId(entity.getSubTypeId());
        form.setParentId(entity.getParentId());
        form.setActive(entity.isActive());
        form.setStartDate(entity.getStartDate());
        form.setEndDate(entity.getEndDate());
        if(form.getSubTypeId() != null){
            form.setSubTypeLabel(entity.getSubType().getLabel());
            form.setSubTypeCode(entity.getSubType().getCode());
            form.setSubTypeDescription(entity.getSubType().getDescription());
            form.setSubTypeDataType(entity.getSubType().getDataType());
        }
        if(form.getTypeId() != null){
            form.setTypeLabel(entity.getType().getLabel());
            form.setTypeCode(entity.getType().getCode());
            form.setTypeDescription(entity.getType().getDescription());
            form.setTypeDataType(entity.getType().getDataType());
        }
        if(form.getParentId() != null){
            form.setParentLabel(entity.getParent().getLabel());
            form.setParentCode(entity.getParent().getCode());
            form.setParentDescription(entity.getParent().getDescription());
            form.setParentDataType(entity.getParent().getDataType());
        }
        return form;
    }
}
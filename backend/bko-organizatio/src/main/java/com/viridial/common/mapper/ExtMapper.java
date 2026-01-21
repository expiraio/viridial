package com.viridial.common.mapper;

import com.viridial.common.form.BaseForm;
import com.viridial.common.entities.BaseEntity;

public class ExtMapper {

    public void toForm(BaseEntity entity, BaseForm form) {
       form.setId(entity.getId());
       form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
       form.setDeletedBy(entity.getDeletedBy());
       form.setCreatedBy(entity.getCreatedBy());
       form.setUpdatedBy(entity.getUpdatedBy());    
       form.setVersion(entity.getVersion());
    }
}

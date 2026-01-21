package com.viridial.organization.mapper;

import com.viridial.organization.entities.TeamEntity;
import com.viridial.organization.forms.TeamForm;

/**
 * Mapper for converting between TeamEntity and TeamForm.
 */
public class TeamMapper {
    public static TeamForm mapEntityToForm(TeamEntity entity) {
        if (entity == null) return null;
        
        TeamForm form = new TeamForm();
        form.setId(entity.getId());
        form.setInternalCode(entity.getInternalCode());
        form.setExternalCode(entity.getExternalCode());
        form.setName(entity.getName());
        form.setDescription(entity.getDescription());
        form.setEmail(entity.getEmail());
        form.setWebsite(entity.getWebsite());
        form.setTaxId(entity.getTaxId());
        form.setVatNumber(entity.getVatNumber());
        form.setLogoUrl(entity.getLogoUrl());
        form.setFoundedDate(entity.getFoundedDate());
        form.setEmployeeCount(entity.getEmployeeCount());
        form.setNotes(entity.getNotes());
        form.setActive(entity.isActive());
        form.setParentId(entity.getParentId());
        form.setTeamTypeId(entity.getTeamTypeId());
        form.setIndustryId(entity.getIndustryId());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


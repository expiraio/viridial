package com.viridial.countries.mapper;

import com.viridial.countries.entities.CountryEntity;
import com.viridial.countries.forms.CountryForm;

/**
 * Mapper for converting between CountryEntity and CountryForm.
 */
public class CountryMapper {
    public static CountryForm mapEntityToForm(CountryEntity entity) {
        if (entity == null) return null;
        
        CountryForm form = new CountryForm();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setNativeName(entity.getNativeName());
        form.setIso2(entity.getIso2());
        form.setIso3(entity.getIso3());
        form.setNumericCode(entity.getNumericCode());
        form.setPhoneCode(entity.getPhoneCode());
        form.setCurrencyCode(entity.getCurrencyCode());
        form.setDomain(entity.getDomain());
        form.setFlagEmoji(entity.getFlagEmoji());
        form.setRegionId(entity.getRegionId());
        form.setSubRegionId(entity.getSubRegionId());
        form.setLanguageId(entity.getLanguageId());
        form.setTimezoneId(entity.getTimezoneId());
        form.setCapitalCityId(entity.getCapitalCityId());
        form.setEnabled(entity.isEnabled());
        form.setActive(entity.isActive());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


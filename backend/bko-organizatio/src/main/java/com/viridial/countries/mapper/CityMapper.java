package com.viridial.countries.mapper;

import com.viridial.countries.entities.CityEntity;
import com.viridial.countries.forms.CityForm;

/**
 * Mapper for converting between CityEntity and CityForm.
 */
public class CityMapper {
    public static CityForm mapEntityToForm(CityEntity entity) {
        if (entity == null) return null;
        
        CityForm form = new CityForm();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setNativeName(entity.getNativeName());
        form.setState(entity.getState());
        form.setDistrict(entity.getDistrict());
        form.setPostalCode(entity.getPostalCode());
        form.setLatitude(entity.getLatitude());
        form.setLongitude(entity.getLongitude());
        form.setElevation(entity.getElevation());
        form.setPopulation(entity.getPopulation());
        form.setAreaKm2(entity.getAreaKm2());
        form.setTimezoneId(entity.getTimezoneId());
        form.setCountryId(entity.getCountryId());
        form.setCapital(entity.isCapital());
        form.setMetropolitan(entity.isMetropolitan());
        form.setActive(entity.isActive());
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        return form;
    }
}


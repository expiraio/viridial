package com.viridial.countries.services;

import com.viridial.countries.forms.CityForm;
import com.viridial.countries.forms.CitySearchForm;

import java.util.List;

/**
 * Service interface for CityEntity operations.
 */
public interface CityService {
    List<CityForm> search(CitySearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


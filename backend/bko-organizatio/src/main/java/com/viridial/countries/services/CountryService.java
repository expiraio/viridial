package com.viridial.countries.services;

import com.viridial.countries.forms.CountryForm;
import com.viridial.countries.forms.CountrySearchForm;

import java.util.List;

/**
 * Service interface for CountryEntity operations.
 */
public interface CountryService {
    List<CountryForm> search(CountrySearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


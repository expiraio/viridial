package com.viridial.countries.services;

import com.viridial.countries.forms.TimezoneForm;
import com.viridial.countries.forms.TimezoneSearchForm;

import java.util.List;

/**
 * Service interface for TimezoneEntity operations.
 */
public interface TimezoneService {
    List<TimezoneForm> search(TimezoneSearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


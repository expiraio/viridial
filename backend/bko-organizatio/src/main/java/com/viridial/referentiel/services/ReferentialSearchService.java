package com.viridial.referentiel.services;

import com.viridial.common.forms.PaginatedResponse;
import com.viridial.referentiel.forms.FormSearch;
import com.viridial.referentiel.forms.ReferentialForm;

public interface ReferentialSearchService {
    PaginatedResponse<ReferentialForm> search(FormSearch search);

}
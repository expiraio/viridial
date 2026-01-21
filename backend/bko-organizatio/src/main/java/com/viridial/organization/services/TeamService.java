package com.viridial.organization.services;

import com.viridial.organization.forms.TeamForm;
import com.viridial.organization.forms.TeamSearchForm;

import java.util.List;

/**
 * Service interface for TeamEntity operations.
 */
public interface TeamService {
    List<TeamForm> search(TeamSearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


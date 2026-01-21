package com.viridial.roles.services;

import com.viridial.roles.forms.RoleForm;
import com.viridial.roles.forms.RoleSearchForm;

import java.util.List;

/**
 * Service interface for RoleEntity operations.
 */
public interface RoleService {
    List<RoleForm> search(RoleSearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


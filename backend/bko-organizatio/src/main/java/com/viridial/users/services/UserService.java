package com.viridial.users.services;

import com.viridial.users.forms.UserForm;
import com.viridial.users.forms.UserSearchForm;

import java.util.List;

/**
 * Service interface for UserEntity operations.
 */
public interface UserService {
    List<UserForm> search(UserSearchForm search);
    int bulkUpdateActive(List<Long> ids, boolean active);
    int bulkDelete(List<Long> ids, String deletedBy);
}


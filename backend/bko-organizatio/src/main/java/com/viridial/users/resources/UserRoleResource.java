package com.viridial.users.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridial.users.services.UserRoleService;

@RestController
@RequestMapping("/user-roles")
public class UserRoleResource {
    @Autowired
    private UserRoleService userRoleService;

}


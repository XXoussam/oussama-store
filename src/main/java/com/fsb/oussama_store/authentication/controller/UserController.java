package com.fsb.oussama_store.authentication.controller;

import com.fsb.oussama_store.authentication.Service.UserService;
import com.fsb.oussama_store.authentication.dao.UserDao;
import com.fsb.oussama_store.authentication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUser();
    }

    @PostMapping({"/registerNewUser"})
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @GetMapping({"/forOwner"})
    @PreAuthorize("hasRole('Owner')")
    public String forAdmin() {
        return "this URL is only accessible to owner";
    }

    @GetMapping({"/forClient"})
    @PreAuthorize("hasRole('Client')")
    public String forUser() {
        return "this URL is only accessible to client";
    }

    @GetMapping({"/getusername"})
    @PreAuthorize("hasRole('Client')")
    public String getCurrentUsername() {
        return userService.getCurrentUsername();
    }

}

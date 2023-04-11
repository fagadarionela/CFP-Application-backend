package com.app.cfp.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@Configuration
public class UserController {
    @RequestMapping(value = "/login")
    public Principal login(Principal principal) {
        return principal;
    }
}

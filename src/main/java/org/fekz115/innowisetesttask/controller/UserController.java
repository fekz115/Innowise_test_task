package org.fekz115.innowisetesttask.controller;

import lombok.AllArgsConstructor;
import org.fekz115.innowisetesttask.model.Role;
import org.fekz115.innowisetesttask.model.User;
import org.fekz115.innowisetesttask.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("login")
    String login(String login, String password) {
        return userService.getToken(login, password);
    }

    @GetMapping("register")
    Optional<User> register(String login, String password) {
        return userService.register(login, password);
    }

    @GetMapping("getRoles")
    Optional<Set<Role>> getRoles(Principal principal) {
        return getUserByPrincipal(principal).map(User::getRoles);
    }

    @GetMapping("me")
    Optional<User> getMe(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private Optional<User> getUserByPrincipal(Principal principal) {
        return userService.getUserByName(principal.getName());
    }
}

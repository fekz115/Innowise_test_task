package org.fekz115.innowisetesttask.service;

import lombok.AllArgsConstructor;
import org.fekz115.innowisetesttask.model.Role;
import org.fekz115.innowisetesttask.model.User;
import org.fekz115.innowisetesttask.repository.UserRepository;
import org.fekz115.innowisetesttask.service.jwt.JwtTokenUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> register(String login, String password) {
        try {
            User user = new User(0, login, password, Collections.singleton(Role.USER), Collections.emptySet());
            return Optional.of(repository.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getToken(String login, String password) throws BadCredentialsException {
        User user = repository.findByLoginAndPassword(login, password).orElseThrow(() -> new BadCredentialsException("Incorrect login or password"));
        return jwtTokenUtil.generateToken(user);
    }

    public Optional<User> getUserByName(String login) {
        return repository.findByLogin(login);
    }
}

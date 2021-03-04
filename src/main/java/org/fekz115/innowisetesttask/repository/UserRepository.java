package org.fekz115.innowisetesttask.repository;

import org.fekz115.innowisetesttask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);

}

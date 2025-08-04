package com.tracker.tasks.services;

import com.tracker.tasks.domain.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void saveUser(User user);
}

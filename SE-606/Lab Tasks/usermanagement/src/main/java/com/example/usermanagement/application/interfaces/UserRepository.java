package com.example.usermanagement.application.interfaces;

import com.example.usermanagement.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for User repository in the application layer.
 * This is implemented by adapters in the infrastructure layer.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
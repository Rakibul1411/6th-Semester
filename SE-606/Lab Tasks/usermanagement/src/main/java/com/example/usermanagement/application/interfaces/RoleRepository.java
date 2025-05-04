package com.example.usermanagement.application.interfaces;

import com.example.usermanagement.domain.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for Role repository in the application layer.
 * This is implemented by adapters in the infrastructure layer.
 */
public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    List<Role> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
package com.example.usermanagement.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User domain entity representing a user in the system.
 * Pure domain entity with no framework annotations.
 */
public class User {
    private final UUID id;
    private String name;
    private String email;
    private final Set<Role> roles;

    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.roles = new HashSet<>();
    }

    // Constructor with ID for reconstruction from persistence
    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    public void assignRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.removeIf(r -> r.getId().equals(role.getId()));
    }
}
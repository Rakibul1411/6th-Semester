package com.example.usermanagement.domain;

import java.util.UUID;

/**
 * Role domain entity representing a role in the system.
 * Pure domain entity with no framework annotations.
 */
public class Role {
    private final UUID id;
    private String roleName;

    public Role(String roleName) {
        this.id = UUID.randomUUID();
        this.roleName = roleName;
    }

    // Constructor with ID for reconstruction from persistence
    public Role(UUID id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public UUID getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
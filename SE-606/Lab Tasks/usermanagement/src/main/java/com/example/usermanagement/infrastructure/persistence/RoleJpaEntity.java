package com.example.usermanagement.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * JPA entity for Role persistence.
 */
@Entity
@Table(name = "roles")
public class RoleJpaEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    // Default constructor required by JPA
    public RoleJpaEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        RoleJpaEntity that = (RoleJpaEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
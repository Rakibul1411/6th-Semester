package com.example.usermanagement.infrastructure.controller.dto;

import com.example.usermanagement.domain.Role;

import java.util.UUID;

/**
 * DTO for role responses.
 */
public class RoleResponseDto {
    private UUID id;
    private String roleName;

    public static RoleResponseDto fromDomain(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        return dto;
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
}
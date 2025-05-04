package com.example.usermanagement.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for role creation requests.
 */
public class CreateRoleRequestDto {
    @NotBlank(message = "Role name is required")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
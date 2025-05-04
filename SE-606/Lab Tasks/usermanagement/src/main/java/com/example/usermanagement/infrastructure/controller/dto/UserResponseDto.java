package com.example.usermanagement.infrastructure.controller.dto;

import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO for user responses including roles.
 */
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private Set<RoleDto> roles;

    public static UserResponseDto fromDomain(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        Set<RoleDto> roleDtos = user.getRoles().stream()
                .map(role -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setId(role.getId());
                    roleDto.setRoleName(role.getRoleName());
                    return roleDto;
                })
                .collect(Collectors.toSet());

        dto.setRoles(roleDtos);

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    /**
     * Nested DTO for role information within user responses.
     */
    public static class RoleDto {
        private UUID id;
        private String roleName;

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
}
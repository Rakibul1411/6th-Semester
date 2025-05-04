package com.example.usermanagement.application;

import com.example.usermanagement.application.exceptions.ResourceNotFoundException;
import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.domain.Role;

import java.util.List;
import java.util.UUID;

/**
 * Service class for Role-related use cases.
 * Depends only on domain entities and repository interfaces.
 */
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Create a new role with the given name.
     *
     * @param roleName Role name
     * @return Created Role's ID
     */
    public UUID createRole(String roleName) {
        Role role = new Role(roleName);
        Role savedRole = roleRepository.save(role);
        return savedRole.getId();
    }

    /**
     * Get role by ID.
     *
     * @param roleId Role's ID
     * @return Role
     * @throws ResourceNotFoundException if role not found
     */
    public Role getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
    }

    /**
     * Get all roles.
     *
     * @return List of all roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
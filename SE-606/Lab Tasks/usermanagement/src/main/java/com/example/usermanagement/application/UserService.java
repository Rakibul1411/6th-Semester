package com.example.usermanagement.application;

import com.example.usermanagement.application.exceptions.ResourceNotFoundException;
import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.application.interfaces.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;

import java.util.List;
import java.util.UUID;

/**
 * Service class for User-related use cases.
 * Depends only on domain entities and repository interfaces.
 */
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Create a new user with the given name and email.
     *
     * @param name User's name
     * @param email User's email
     * @return Created User's ID
     */
    public UUID createUser(String name, String email) {
        User user = new User(name, email);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    /**
     * Get user details by ID, including assigned roles.
     *
     * @param userId User's ID
     * @return User with roles
     * @throws ResourceNotFoundException if user not found
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Get all users.
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Assign a role to a user.
     *
     * @param userId User's ID
     * @param roleId Role's ID
     * @throws ResourceNotFoundException if user or role not found
     */
    public void assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        user.assignRole(role);
        userRepository.save(user);
    }

    /**
     * Remove a role from a user.
     *
     * @param userId User's ID
     * @param roleId Role's ID
     * @throws ResourceNotFoundException if user or role not found
     */
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        user.removeRole(role);
        userRepository.save(user);
    }
}
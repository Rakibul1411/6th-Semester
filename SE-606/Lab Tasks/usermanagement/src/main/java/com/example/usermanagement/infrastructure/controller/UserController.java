package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.UserService;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.infrastructure.controller.dto.CreateUserRequestDto;
import com.example.usermanagement.infrastructure.controller.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for user-related endpoints.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user with name and email")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UUID> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        UUID userId = userService.createUser(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user details along with assigned roles")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        UserResponseDto responseDto = UserResponseDto.fromDomain(user);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> responseDtos = users.stream()
                .map(UserResponseDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/{userId}/assign-role/{roleId}")
    @Operation(summary = "Assign role to user", description = "Assign an existing role to an existing user")
    @ApiResponse(responseCode = "200", description = "Role assigned successfully")
    @ApiResponse(responseCode = "404", description = "User or role not found")
    public ResponseEntity<String> assignRoleToUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping("/{userId}/remove-role/{roleId}")
    @Operation(summary = "Remove role from user", description = "Remove a role from a user")
    @ApiResponse(responseCode = "200", description = "Role removed successfully")
    @ApiResponse(responseCode = "404", description = "User or role not found")
    public ResponseEntity<String> removeRoleFromUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok("Role removed successfully");
    }
}
package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.RoleService;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.infrastructure.controller.dto.CreateRoleRequestDto;
import com.example.usermanagement.infrastructure.controller.dto.RoleResponseDto;
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
 * REST controller for role-related endpoints.
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "Role Management", description = "APIs for role management")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @Operation(summary = "Create a new role", description = "Create a new role with role name")
    @ApiResponse(responseCode = "201", description = "Role created successfully")
    public ResponseEntity<UUID> createRole(@Valid @RequestBody CreateRoleRequestDto request) {
        UUID roleId = roleService.createRole(request.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(roleId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieve role details by ID")
    @ApiResponse(responseCode = "200", description = "Role found")
    @ApiResponse(responseCode = "404", description = "Role not found")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable UUID id) {
        Role role = roleService.getRoleById(id);
        RoleResponseDto responseDto = RoleResponseDto.fromDomain(role);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieve all roles")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDto> responseDtos = roles.stream()
                .map(RoleResponseDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}
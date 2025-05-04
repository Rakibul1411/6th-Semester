package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.domain.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements the RoleRepository interface from the application layer
 * and uses Spring Data JPA repository for persistence.
 */
@Component
public class RoleRepositoryAdapter implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Role save(Role role) {
        RoleJpaEntity roleJpaEntity = toJpaEntity(role);
        RoleJpaEntity savedEntity = roleJpaRepository.save(roleJpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        roleJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return roleJpaRepository.existsById(id);
    }

    /**
     * Convert from domain Role to JPA RoleJpaEntity
     */
    private RoleJpaEntity toJpaEntity(Role role) {
        RoleJpaEntity roleJpaEntity = new RoleJpaEntity();
        roleJpaEntity.setId(role.getId());
        roleJpaEntity.setRoleName(role.getRoleName());
        return roleJpaEntity;
    }

    /**
     * Convert from JPA RoleJpaEntity to domain Role
     */
    private Role toDomainEntity(RoleJpaEntity roleJpaEntity) {
        return new Role(
                roleJpaEntity.getId(),
                roleJpaEntity.getRoleName()
        );
    }
}
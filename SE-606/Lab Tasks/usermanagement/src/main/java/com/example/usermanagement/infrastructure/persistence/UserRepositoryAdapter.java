package com.example.usermanagement.infrastructure.persistence;

import com.example.usermanagement.application.interfaces.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements the UserRepository interface from the application layer
 * and uses Spring Data JPA repository for persistence.
 */
@Component
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }

    /**
     * Convert from domain User to JPA UserJpaEntity
     */
    private UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setId(user.getId());
        userJpaEntity.setName(user.getName());
        userJpaEntity.setEmail(user.getEmail());

        // Convert and set roles
        Set<RoleJpaEntity> roleEntities = user.getRoles().stream()
                .map(role -> {
                    RoleJpaEntity roleJpaEntity = new RoleJpaEntity();
                    roleJpaEntity.setId(role.getId());
                    roleJpaEntity.setRoleName(role.getRoleName());
                    return roleJpaEntity;
                })
                .collect(Collectors.toSet());

        // First clear any existing roles then add the new ones
        userJpaEntity.setRoles(roleEntities);

        return userJpaEntity;
    }

    /**
     * Convert from JPA UserJpaEntity to domain User
     */
    private User toDomainEntity(UserJpaEntity userJpaEntity) {
        User user = new User(
                userJpaEntity.getId(),
                userJpaEntity.getName(),
                userJpaEntity.getEmail()
        );

        // Convert and add roles
        userJpaEntity.getRoles().forEach(roleJpaEntity -> {
            Role role = new Role(
                    roleJpaEntity.getId(),
                    roleJpaEntity.getRoleName()
            );
            user.assignRole(role);
        });

        return user;
    }
}
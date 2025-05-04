package com.example.usermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for RoleJpaEntity.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
}
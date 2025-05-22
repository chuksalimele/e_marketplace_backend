// RoleRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Role;       // Import the Role entity
import com.marketplace.emarketplacebackend.model.Role.ERole; // Import the ERole enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // JpaRepository provides basic CRUD operations for the Role entity.

    // Custom query method: Find a Role entity by its predefined ERole name.
    Optional<Role> findByName(ERole name);
}
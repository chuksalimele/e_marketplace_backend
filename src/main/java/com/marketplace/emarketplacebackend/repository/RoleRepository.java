// RoleRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Role;
import com.marketplace.emarketplacebackend.model.ERole; // NEW IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Change method signature to accept ERole
    Optional<Role> findByName(ERole name); // CHANGE HERE
}
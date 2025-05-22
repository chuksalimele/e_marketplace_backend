// SellerRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Used for methods that might not find a result

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    // JpaRepository provides basic CRUD operations.

    // You can add custom methods here if needed, e.g.,
     Optional<Seller> findByName(String name);
    // List<Seller> findByCity(String city);
}


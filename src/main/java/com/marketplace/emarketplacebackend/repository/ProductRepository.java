// ProductRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Optional but good practice for clarity
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.

    // You can define custom query methods by following Spring Data JPA naming conventions:
    List<Product> findByCategory(String category);
    List<Product> findBySellerId(String sellerId);
    List<Product> findByNameContainingIgnoreCase(String name); // For partial, case-insensitive search by name
    List<Product> findBySellerIdAndCategory(String sellerId, String category);

    // This would be for the "advertised" products, but you might manage this differently
    // like with a boolean flag in the Product entity or a separate table for promotions.
    // For now, let's assume it's just a subset of all products.
    // List<Product> findByIsAdvertisedTrue(); // Example if you add 'isAdvertised' field
}
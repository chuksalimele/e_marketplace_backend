// ProductRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Add this import

import java.util.List;

@Repository // Optional but good practice for clarity
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.

    // You can define custom query methods by following Spring Data JPA naming conventions:
    Optional<Product> findByName(String name); // Add this method
    // Change findByCategory(String category) to findByCategory_Name(String categoryName)
    List<Product> findByCategory_Name(String categoryName); // CORRECTED METHOD
    List<Product> findBySeller_Id(Long sellerId);

    // This would be for the "advertised" products, but you might manage this differently
    // like with a boolean flag in the Product entity or a separate table for promotions.
    // For now, let's assume it's just a subset of all products.
    // List<Product> findByIsAdvertisedTrue(); // Example if you add 'isAdvertised' field
}
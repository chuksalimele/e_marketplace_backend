// ProductRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Product;
import org.springframework.data.domain.Page; // NEW IMPORT
import org.springframework.data.domain.Pageable; // NEW IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; 

import java.util.List;

@Repository // Optional but good practice for clarity
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.

    // You can define custom query methods by following Spring Data JPA naming conventions:
    Optional<Product> findByName(String name); 
    
    // Original methods:
    List<Product> findByCategory_Name(String categoryName); 
    List<Product> findBySeller_Id(Long sellerId);

    // NEW: Methods for filtered products with pagination and sorting
    Page<Product> findByCategory_Name(String categoryName, Pageable pageable);
    Page<Product> findBySeller_Id(Long sellerId, Pageable pageable);

    // You can also add methods for searching by name/description with pagination
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);


}
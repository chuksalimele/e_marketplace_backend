// ProductService.java
package com.marketplace.emarketplacebackend.service;

import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring Service component
public class ProductService {

    private final ProductRepository productRepository;

    // Spring will automatically inject ProductRepository here
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- CRUD Operations ---

    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Inherited from JpaRepository
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id); // Inherited from JpaRepository
    }

    public Product saveProduct(Product product) {
        // Here you might add validation or other business logic before saving
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // --- Custom Query Methods (mirroring repository methods) ---

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsBySellerId(String sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    public List<Product> searchProductsByName(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getProductsBySellerIdAndCategory(String sellerId, String category) {
        return productRepository.findBySellerIdAndCategory(sellerId, category);
    }

    // For "advertised products", if not a specific field,
    // you might have a more complex logic, e.g.,
    // public List<Product> getAdvertisedProducts() {
    //    // Implement logic to pick top/featured products
    //    // For now, let's just return a subset of all products or assume a flag
    //    return productRepository.findAll().subList(0, Math.min(5, productRepository.findAll().size()));
    // }
}
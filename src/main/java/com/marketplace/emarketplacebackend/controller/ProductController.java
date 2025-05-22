// ProductController.java (CORRECTED createProduct method)
package com.marketplace.emarketplacebackend.controller;

import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) // Adjust for Flutter app's port
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    // Get all products - accessible by any authenticated user
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get product by ID - accessible by any authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productData = productRepository.findById(id);
        return productData.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new product - Only for SELLERS or ADMINS
    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            // CORRECTED LINE: Directly save the 'product' object received in the request body.
            // Spring will handle generating the ID if 'product.id' is null.
            Product _product = productRepository.save(product);
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging purposes in a real application
            // logger.error("Error creating product: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing product - Only for SELLERS or ADMINS
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()) {
            Product _product = productData.get();
            // Assuming the 'product' object from @RequestBody contains the updated fields
            // You might want to copy properties or set them explicitly if only partial updates are allowed
            _product.setName(product.getName());
            _product.setImageUrl(product.getImageUrl());
            _product.setPrice(product.getPrice());
            _product.setSellerId(product.getSellerId());
            _product.setSellerName(product.getSellerName());
            _product.setCategory(product.getCategory());
            _product.setWeight(product.getWeight());
            return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a product - Only for ADMINS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
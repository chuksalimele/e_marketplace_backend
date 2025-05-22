// ProductController.java
package com.marketplace.emarketplacebackend.controller;

import com.marketplace.emarketplacebackend.dto.ProductRequest;
import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.service.ProductService;
import jakarta.validation.Valid; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // NEW IMPORT
import org.springframework.data.domain.Pageable; // NEW IMPORT
import org.springframework.data.domain.Sort; // NEW IMPORT
import org.springframework.data.web.PageableDefault; // NEW IMPORT
import org.springframework.data.web.SortDefault; // NEW IMPORT
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Keep this import for non-paginated methods if they still exist

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')") 
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product createdProduct = productService.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // MODIFIED: getAllProducts to support pagination and sorting
    // Example usage: GET /api/products?page=0&size=10&sort=name,asc
    // Default: page=0, size=20, sort by id ascending
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(page = 0, size = 20) // Default page 0, size 20
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) // Default sort by id ascending
            Pageable pageable) {
        
        Page<Product> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')") 
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        Product updatedProduct = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // NEW: Endpoint to get products by category with pagination and sorting
    // Example: GET /api/products/category/Electronics?page=0&size=10&sort=price,desc
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable String categoryName,
            @PageableDefault(page = 0, size = 20)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        Page<Product> products = productService.getProductsByCategory(categoryName, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // NEW: Endpoint to get products by seller with pagination and sorting
    // Example: GET /api/products/seller/1?page=0&size=10&sort=name,desc
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<Product>> getProductsBySeller(
            @PathVariable Long sellerId,
            @PageableDefault(page = 0, size = 20)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        Page<Product> products = productService.getProductsBySeller(sellerId, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // NEW: Endpoint to search products by name or description with pagination and sorting
    // Example: GET /api/products/search?query=laptop&page=0&size=10&sort=price,asc
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String query,
            @PageableDefault(page = 0, size = 20)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        Page<Product> products = productService.searchProducts(query, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
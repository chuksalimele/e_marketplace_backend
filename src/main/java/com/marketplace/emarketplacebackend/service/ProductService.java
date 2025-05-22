package com.marketplace.emarketplacebackend.service;

import com.marketplace.emarketplacebackend.dto.ProductRequest;
import com.marketplace.emarketplacebackend.exception.ResourceNotFoundException;
import com.marketplace.emarketplacebackend.model.Category;
import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.model.Seller;
import com.marketplace.emarketplacebackend.repository.CategoryRepository;
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import com.marketplace.emarketplacebackend.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // NEW IMPORT
import org.springframework.data.domain.Pageable; // NEW IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findByName(productRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", productRequest.getCategoryName()));

        Seller seller = sellerRepository.findById(productRequest.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", productRequest.getSellerId()));

        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStock(),
                seller,
                category
        );
        product.setCategory(category);
        product.setSeller(seller);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category category = categoryRepository.findByName(productRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", productRequest.getCategoryName()));

        Seller seller = sellerRepository.findById(productRequest.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", productRequest.getSellerId()));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock()); 
        existingProduct.setCategory(category);
        existingProduct.setSeller(seller);

        return productRepository.save(existingProduct);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    // MODIFIED: getAllProducts to accept Pageable
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // NEW: Get products by category with pagination and sorting
    public Page<Product> getProductsByCategory(String categoryName, Pageable pageable) {
        return productRepository.findByCategory_Name(categoryName, pageable);
    }

    // NEW: Get products by seller with pagination and sorting
    public Page<Product> getProductsBySeller(Long sellerId, Pageable pageable) {
        return productRepository.findBySeller_Id(sellerId, pageable);
    }

    // NEW: Search products by name or description with pagination and sorting
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        // You can choose to search by name, description, or both
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
    }
}
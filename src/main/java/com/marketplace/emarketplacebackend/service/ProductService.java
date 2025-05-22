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
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findByName(productRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", productRequest.getCategoryName()));

        Seller seller = sellerRepository.findById(productRequest.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", productRequest.getSellerId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock()); // Set the new stock field
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
        existingProduct.setStock(productRequest.getStock()); // Update the new stock field
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
}
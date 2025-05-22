// Product.java
package com.marketplace.emarketplacebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Make sure to adjust these excludes based on your actual bidirectional relationships.
// Assuming 'seller' and 'category' might be bidirectional and need exclusion from equals/hashCode.
@EqualsAndHashCode(exclude = {"seller", "category"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    // NEW FIELD: Stock quantity
    @Column(nullable = false)
    private Integer stock; // Make sure this is initialized or set upon creation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "products"}) // Assuming Seller has a 'products' list
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "products"}) // Assuming Category has a 'products' list
    private Category category;

    // You might want to update your constructors or add a new one
    // to easily set the stock when creating Product objects.
    public Product(String name, String description, Double price, Integer stock, Seller seller, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock; // Initialize stock
        this.seller = seller;
        this.category = category;
    }
}
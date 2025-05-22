// Product.java
package com.marketplace.emarketplacebackend.model;

import jakarta.persistence.*; // JPA annotations
import lombok.AllArgsConstructor; // Lombok for constructor
import lombok.Data;             // Lombok for getters/setters
import lombok.NoArgsConstructor;   // Lombok for constructor

@Entity // Marks this class as a JPA entity
@Table(name = "products") // Specifies the table name in the database
@Data // Lombok: Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok: Generates a constructor with no arguments
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class Product {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID for new entities
    private Long id;

    @Column(name = "name", nullable = false) // Maps to a column named 'name', cannot be null
    private String name;

    @Column(name = "image_url") // Maps to a column named 'image_url'
    private String imageUrl;

    @Column(name = "price", nullable = false) // Maps to 'price', cannot be null
    private Double price;

    @Column(name = "seller_id") // Maps to 'seller_id'
    private String sellerId; // Keeping as String as per your initial model design

    @Column(name = "seller_name") // Maps to 'seller_name'
    private String sellerName;

    @Column(name = "category") // Maps to 'category'
    private String category;

    // --- NEW/VERIFY THIS FIELD ---
    @Column(name = "weight") // Maps to 'weight' column
    private Double weight; // Ensure this field exists and is of appropriate type (e.g., Double or double)

    // Lombok's @Data annotation will automatically generate
    // getWeight() and setWeight() methods for you.
    // If you are not using Lombok, you would need to add these manually:
    /*
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    */
}
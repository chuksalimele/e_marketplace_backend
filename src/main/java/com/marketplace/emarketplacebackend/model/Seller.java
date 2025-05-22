// Seller.java
package com.marketplace.emarketplacebackend.model;

import jakarta.persistence.*; // JPA annotations
import lombok.AllArgsConstructor; // Lombok for constructor
import lombok.Data;             // Lombok for getters/setters
import lombok.NoArgsConstructor;   // Lombok for constructor

@Entity // Marks this class as a JPA entity
@Table(name = "sellers") // Specifies the table name in the database
@Data // Lombok: Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok: Generates a constructor with no arguments
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class Seller {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID for new entities
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // --- NEW/VERIFY THIS FIELD ---
    @Column(name = "location") // Maps to 'location' column
    private String location; // Ensure this field exists

    // --- NEW/VERIFY THIS FIELD ---
    @Column(name = "rating") // Maps to 'rating' column
    private Integer rating; // Or 'Double' if you prefer decimal ratings

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "description", length = 1000) // Increased length for description
    private String description;

    @Column(name = "categories")
    private String categories; // Comma-separated string of categories

    // Lombok's @Data annotation will automatically generate
    // getLocation(), setLocation(), getRating(), setRating() methods for you.
    // If you are not using Lombok, you would need to add these manually.
}
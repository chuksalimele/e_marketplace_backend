// Seller.java
package com.marketplace.emarketplacebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode; // Assuming you're using this for @EqualsAndHashCode.Exclude
import lombok.NoArgsConstructor;
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // If you use it for bidirectional relationships

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor // This will now generate a constructor with all 8 fields (id, name, email, location, etc.)
// If Seller has a products list, remember to exclude it from equals/hashCode
// @EqualsAndHashCode(exclude = {"products"})
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true) // Added unique=true as names are often unique
    private String name;

    @Column(name = "email", nullable = false, unique = true) // ADD THIS FIELD BACK
    private String email;

    @Column(name = "location")
    private String location;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "categories")
    private String categories; // Comma-separated string of categories

    // OPTIONAL: Add a custom constructor for common initializations if you don't want to pass all fields
     public Seller(String name, String email) {
         this.name = name;
         this.email = email;
         this.location = null; // Or ""
         this.rating = 0; // Or null
         this.profileImageUrl = null; // Or ""
         this.description = null; // Or ""
         this.categories = null; // Or ""
    }
}
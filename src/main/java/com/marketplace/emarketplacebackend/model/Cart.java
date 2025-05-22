// Cart.java
package com.marketplace.emarketplacebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One relationship with User
    // Each user has one cart. FetchType.LAZY to avoid loading user unnecessarily.
    // @JoinColumn specifies the foreign key column in the 'carts' table.
    // unique = true ensures only one cart per user.
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    // One-to-Many relationship with CartItem
    // A cart can have multiple cart items.
    // mappedBy refers to the 'cart' field in the CartItem entity.
    // CascadeType.ALL ensures that if a Cart is deleted, all its CartItems are also deleted.
    // orphanRemoval = true ensures that if a CartItem is removed from the 'items' set, it's deleted from the DB.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CartItem> items = new HashSet<>(); // Use a Set to prevent duplicate cart items

    // You might want to add a constructor that takes a User
    public Cart(User user) {
        this.user = user;
    }
}
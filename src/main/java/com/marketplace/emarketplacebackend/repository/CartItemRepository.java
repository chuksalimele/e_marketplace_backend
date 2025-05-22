// CartItemRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Cart;
import com.marketplace.emarketplacebackend.model.CartItem;
import com.marketplace.emarketplacebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Custom method to find a CartItem by Cart and Product
    // This is useful for checking if a product is already in the cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Optional: Delete a CartItem by Cart and Product (if not using orphanRemoval via entity relationship)
    // int deleteByCartAndProduct(Cart cart, Product product);
}
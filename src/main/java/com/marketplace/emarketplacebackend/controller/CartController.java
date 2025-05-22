// CartController.java
package com.marketplace.emarketplacebackend.controller;

import com.marketplace.emarketplacebackend.model.Cart;
import com.marketplace.emarketplacebackend.model.CartItem;
import com.marketplace.emarketplacebackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Endpoint to add a product to the authenticated user's cart.
     * If the product is already in the cart, its quantity will be updated.
     * Requires the user to be authenticated.
     *
     * Request Body example:
     * {
     * "productId": 1,
     * "quantity": 2
     * }
     */
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()") // Only authenticated users can add to cart
    public ResponseEntity<CartItem> addProductToCart(@RequestBody Map<String, Long> payload) {
        // We expect productId and quantity from the request body
        Long productId = payload.get("productId");
        Integer quantity = payload.get("quantity").intValue(); // Convert Long to Integer

        if (productId == null || quantity == null || quantity <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CartItem updatedCartItem = cartService.addItemToCart(productId, quantity);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve the authenticated user's entire cart.
     * Requires the user to be authenticated.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Only authenticated users can view their cart
    public ResponseEntity<Cart> getUserCart() {
        Cart userCart = cartService.getUserCart();
        return new ResponseEntity<>(userCart, HttpStatus.OK);
    }

    // --- Future methods to add: ---
    // @PutMapping("/update") for changing quantities
    // @DeleteMapping("/remove/{productId}") for removing specific items
    // @DeleteMapping("/clear") for clearing the entire cart
}
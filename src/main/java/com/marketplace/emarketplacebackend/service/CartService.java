// CartService.java
package com.marketplace.emarketplacebackend.service;

import com.marketplace.emarketplacebackend.exception.ResourceNotFoundException;
import com.marketplace.emarketplacebackend.model.Cart;
import com.marketplace.emarketplacebackend.model.CartItem;
import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.model.User;
import com.marketplace.emarketplacebackend.repository.CartItemRepository;
import com.marketplace.emarketplacebackend.repository.CartRepository;
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import com.marketplace.emarketplacebackend.repository.UserRepository; // We need this to get the User object
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional annotation

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // To fetch the full User object

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the currently authenticated user from the SecurityContextHolder.
     * Throws ResourceNotFoundException if user is not found (shouldn't happen if authenticated).
     */
    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Finds the cart for the current user, or creates a new one if it doesn't exist.
     * @return The user's Cart.
     */
    @Transactional // Ensures the operation is atomic and manages persistence context
    public Cart getOrCreateCartForCurrentUser() {
        User currentUser = getCurrentAuthenticatedUser();
        return cartRepository.findByUser(currentUser)
                .orElseGet(() -> {
                    Cart newCart = new Cart(currentUser);
                    return cartRepository.save(newCart); // Save the new cart
                });
    }

    /**
     * Adds a product to the current user's cart or updates its quantity if already present.
     * @param productId The ID of the product to add.
     * @param quantity The quantity to add/update.
     * @return The updated CartItem.
     * @throws ResourceNotFoundException if the product is not found.
     * @throws IllegalArgumentException if quantity is invalid.
     */
    @Transactional
    public CartItem addItemToCart(Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart userCart = getOrCreateCartForCurrentUser(); // Get or create cart for current user

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the product is already in the cart
        Optional<CartItem> existingCartItemOptional = cartItemRepository.findByCartAndProduct(userCart, product);

        CartItem cartItem;
        if (existingCartItemOptional.isPresent()) {
            // Product already in cart, update quantity
            cartItem = existingCartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Add to existing quantity
            // No need to explicitly save cartItem due to @Transactional and managed entity state
        } else {
            // Product not in cart, create new CartItem
            cartItem = new CartItem(userCart, product, quantity);
            userCart.getItems().add(cartItem); // Add to the cart's collection
            // No need to explicitly save cartItem here, as it will be cascaded when cart is saved/merged
        }

        // Saving the cart will cascade the changes to cart items (due to CascadeType.ALL)
        cartRepository.save(userCart);

        return cartItem;
    }

    /**
     * Retrieves the current user's cart with all its items.
     * @return The Cart object of the current user.
     * @throws ResourceNotFoundException if the user or their cart is not found.
     */
    @Transactional(readOnly = true) // Read-only transaction for fetching data
    public Cart getUserCart() {
        User currentUser = getCurrentAuthenticatedUser();
        // Use findByUser and then load items explicitly if FetchType.LAZY was used,
        // or ensure the initial findByUser fetches items if needed (e.g. by JOIN FETCH)
        // For simplicity, getOrCreateCartForCurrentUser already handles loading the cart.
        Cart userCart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + currentUser.getUsername()));

        // Ensure cart items are loaded if FetchType.LAZY is used on the items set
        userCart.getItems().size(); // Trigger loading of lazy-loaded items within the transaction

        return userCart;
    }

    // You will add more methods here later:
    // - updateCartItemQuantity(productId, newQuantity)
    // - removeCartItem(productId)
    // - clearCart()
}
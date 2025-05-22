// EmarketplaceBackendApplication.java (modified)
package com.marketplace.emarketplacebackend;

import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.model.Role; // NEW
import com.marketplace.emarketplacebackend.model.Role.ERole; // NEW
import com.marketplace.emarketplacebackend.model.Seller;
import com.marketplace.emarketplacebackend.model.User; // NEW
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import com.marketplace.emarketplacebackend.repository.RoleRepository; // NEW
import com.marketplace.emarketplacebackend.repository.SellerRepository;
import com.marketplace.emarketplacebackend.repository.UserRepository; // NEW
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder; // NEW

import java.util.Arrays;
import java.util.HashSet; // NEW
import java.util.List;
import java.util.Set; // NEW

@SpringBootApplication
public class EMarketplaceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EMarketplaceBackendApplication.class, args);
    }

    // Combine or create a new CommandLineRunner for initial data and roles
    @Bean
    public CommandLineRunner demoAndRoleData(
            ProductRepository productRepository,
            SellerRepository sellerRepository,
            RoleRepository roleRepository, // NEW
            UserRepository userRepository,   // NEW
            PasswordEncoder passwordEncoder // NEW - for encoding initial user password
    ) {
        return args -> {
            // --- Clear existing data (for fresh start in H2) ---
            userRepository.deleteAll(); // NEW: Clear users first (due to foreign key constraints with roles)
            productRepository.deleteAll();
            sellerRepository.deleteAll();
            roleRepository.deleteAll(); // NEW: Clear roles

            // --- Initialize Roles (IMPORTANT!) ---
            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_USER));
            }
            if (roleRepository.findByName(ERole.ROLE_SELLER).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_SELLER));
            }
            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }
            System.out.println("Roles initialized successfully.");

            // --- Create some dummy users with roles ---
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
            Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER).orElseThrow();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();

            // User 1: Regular User
            User user1 = new User("testuser", "user@example.com", passwordEncoder.encode("password123"));
            Set<Role> user1Roles = new HashSet<>();
            user1Roles.add(userRole);
            user1.setRoles(user1Roles);
            userRepository.save(user1);

            // User 2: Seller
            User user2 = new User("testseller", "seller@example.com", passwordEncoder.encode("password123"));
            Set<Role> user2Roles = new HashSet<>();
            user2Roles.add(userRole); // Sellers also have basic user role
            user2Roles.add(sellerRole);
            user2.setRoles(user2Roles);
            userRepository.save(user2);

            // User 3: Admin
            User user3 = new User("admin", "admin@example.com", passwordEncoder.encode("adminpassword"));
            Set<Role> user3Roles = new HashSet<>();
            user3Roles.add(userRole);
            user3Roles.add(adminRole);
            user3.setRoles(user3Roles);
            userRepository.save(user3);

            System.out.println("Dummy users created successfully.");


            // --- Create some dummy sellers (your existing data) ---
            List<Seller> sellers = Arrays.asList(
                new Seller(null, "Mega Electronics", "Onitsha", 3, "assets/seller_profile_1.jpg", "Your one-stop shop for all electronics.", "Electronics,Home Goods"),
                new Seller(null, "Fresh Groceries", "Onitsha", 2, "assets/seller_profile_2.jpg", "Fresh and organic produce delivered to your door.", "Groceries"),
                new Seller(null, "Fashion Hub", "Onitsha", 1, "assets/seller_profile_3.jpg", "Latest fashion trends for men and women.", "Fashion"),
                new Seller(null, "Bookworm Store", "Onitsha", 2, "assets/seller_profile_4.jpg", "A wide range of books for all ages.", "Books"),
                new Seller(null, "Sporty Gear", "Onitsha", 3, "assets/seller_profile_5.jpg", "High-quality sports equipment and apparel.", "Sports"),
                new Seller(null, "Glamour Beauty", "Onitsha", 1, "assets/seller_profile_6.jpg", "Premium beauty products and cosmetics.", "Beauty"),
                new Seller(null, "Home Comforts", "Onitsha", 2, "assets/seller_profile_7.jpg", "Everything for your home, from decor to appliances.", "Home Goods")
            );
            sellerRepository.saveAll(sellers);

            // Fetch saved sellers to get their generated IDs for products
            // (Note: sellerId in Product is String, linking by name for dummy data)
            // You might want to refine this to use actual Long IDs if Seller.id changes to String.
            // For now, mapping using names as before.
            // Ensure findByName method exists in SellerRepository as per our previous fix.

            // The following fetch by name logic assumes Seller.name is unique enough for dummy data
            // In a real app, you might have a direct foreign key relationship (Product.sellerId = Seller.id)
            // Or you would have created the products *after* saving sellers and assigned their actual Long IDs.
            // For now, let's keep the product sellerId as string as per current Product model
            // and rely on existing approach for dummy data linking via a string `sellerId` like "s1", "s2".
            // If you change Product.sellerId to Long, you would need to fetch seller IDs and assign them here.

            List<Product> products = Arrays.asList(
                new Product(null, "Smartphone X", "assets/product_images/phone_1.jpg", 120000.00, "s1", "Mega Electronics", "Electronics", 2.5),
                new Product(null, "Organic Apples (1kg)", "assets/product_images/apples_1.jpg", 1500.00, "s2", "Fresh Groceries", "Groceries", 1.2),
                new Product(null, "Stylish Men's Shirt", "assets/product_images/shirt_1.jpg", 8500.00, "s3", "Fashion Hub", "Fashion", 3.8),
                new Product(null, "Laptop Pro 15", "assets/product_images/laptop_1.jpg", 350000.00, "s1", "Mega Electronics", "Electronics", 2.7),
                new Product(null, "Fresh Tomatoes (500g)", "assets/product_images/tomatoes_1.jpg", 800.00, "s2", "Fresh Groceries", "Groceries", 1.5),
                new Product(null, "Women's Casual Dress", "assets/product_images/dress_1.jpg", 12000.00, "s3", "Fashion Hub", "Fashion", 4.0),
                new Product(null, "The Great Gatsby", "assets/product_images/book_1.jpg", 4500.00, "s4", "Bookworm Store", "Books", 0.9),
                new Product(null, "Basketball Hoop", "assets/product_images/basketball_hoop_1.jpg", 75000.00, "s5", "Sporty Gear", "Sports", 5.1),
                new Product(null, "Facial Cleanser", "assets/product_images/cleanser_1.jpg", 6000.00, "s6", "Glamour Beauty", "Beauty", 2.0),
                new Product(null, "Smart Television", "assets/product_images/tv_1.jpg", 250000.00, "s1", "Mega Electronics", "Electronics", 2.9),
                new Product(null, "Blender", "assets/product_images/blender_1.jpg", 22000.00, "s7", "Home Comforts", "Home Goods", 1.8),
                new Product(null, "Running Shoes", "assets/product_images/shoes_1.jpg", 18000.00, "s5", "Sporty Gear", "Sports", 5.5)
            );
            productRepository.saveAll(products);

            System.out.println("Dummy data loaded successfully!");
        };
    }
}
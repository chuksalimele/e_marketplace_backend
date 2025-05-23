// EMarketplaceBackendApplication.java
package com.marketplace.emarketplacebackend;

import com.marketplace.emarketplacebackend.model.Category;
import com.marketplace.emarketplacebackend.model.ERole;
import com.marketplace.emarketplacebackend.model.Product;
import com.marketplace.emarketplacebackend.model.Role;
import com.marketplace.emarketplacebackend.model.Seller;
import com.marketplace.emarketplacebackend.model.Store;
import com.marketplace.emarketplacebackend.model.User;
import com.marketplace.emarketplacebackend.repository.CategoryRepository;
import com.marketplace.emarketplacebackend.repository.ProductRepository;
import com.marketplace.emarketplacebackend.repository.RoleRepository;
import com.marketplace.emarketplacebackend.repository.SellerRepository;
import com.marketplace.emarketplacebackend.repository.StoreRepository;
import com.marketplace.emarketplacebackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class EMarketplaceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EMarketplaceBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SellerRepository sellerRepository, // Inject SellerRepository
            StoreRepository storeRepository, // Inject SellerRepository
            CategoryRepository categoryRepository, // Inject CategoryRepository
            ProductRepository productRepository // Inject ProductRepository
    ) {
        return args -> {
            // 1. Create Roles if they don't exist
            // Use ERole.ROLE_USER.name() to get the String for creation if the Role entity
            // still expects a String for creation, but findByName expects ERole.
            // This suggests a slight mismatch in initial role creation vs. lookup.
            // If Role.name is truly ERole, then save should take ERole directly or a Role object with ERole.
            // For now, let's assume Role constructor takes String, but findByName takes ERole.
            // BEST PRACTICE: Adjust Role model constructor to take ERole.
            // Let's modify Role.java to accept ERole for better type safety.

            // Ensure your Role entity has a constructor like:
            // public Role(ERole name) { this.name = name; }
            // And its 'name' field is @Enumerated(EnumType.STRING) private ERole name;

            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) { // CHANGE HERE
                roleRepository.save(new Role(ERole.ROLE_USER)); // Assuming Role constructor takes ERole
            }
            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) { // CHANGE HERE
                roleRepository.save(new Role(ERole.ROLE_ADMIN)); // Assuming Role constructor takes ERole
            }
            if (roleRepository.findByName(ERole.ROLE_SELLER).isEmpty()) { // CHANGE HERE
                roleRepository.save(new Role(ERole.ROLE_SELLER)); // Assuming Role constructor takes ERole
            }

            // 2. Create Admin User
            Optional<User> adminUser = userRepository.findByUsername("admin");
            if (adminUser.isEmpty()) {
                // CHANGE HERE: Pass ERole.ROLE_ADMIN to findByName
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Admin role not found!"));
                
                User admin = new User(
                    null,                                  // id (auto-generated, so null for new entity)
                    "admin",                               // username
                    "admin@example.com",                   // email
                    passwordEncoder.encode("password"),    // password (hashed)
                    Set.of(adminRole),                     // roles
                    null                                   // cart (initially null, it will be created implicitly or explicitly later)
                );
                userRepository.save(admin);
                System.out.println("Admin user created.");
            }



            // 3. Create a Seller (if not exists)
            Optional<Seller> existingSeller = sellerRepository.findByName("Sporty Gear");
            Seller sportyGearSeller;
            if (existingSeller.isEmpty()) {
                sportyGearSeller = new Seller("Sporty Gear", "sportygear@example.com");
                sportyGearSeller = sellerRepository.save(sportyGearSeller);
                System.out.println("Seller 'Sporty Gear' created.");
            } else {
                sportyGearSeller = existingSeller.get();
            }

            // 4. Create a Seller (if not exists)
            Optional<Store> existingStore = storeRepository.findByName("Sporty");
            Store sportyStore;
            if (existingStore.isEmpty()) {
                sportyStore = new Store("Sporty", "Warri", sportyGearSeller);
                sportyStore = storeRepository.save(sportyStore);
                System.out.println("Store 'Sporty' created.");
            } else {
                sportyStore = existingStore.get();
            }

            // 5. Create Categories (if not exists)
            Optional<Category> existingSportsCategory = categoryRepository.findByName("Sports");
            Category sportsCategory;
            if (existingSportsCategory.isEmpty()) {
                sportsCategory = new Category("Sports");
                sportsCategory = categoryRepository.save(sportsCategory);
                System.out.println("Category 'Sports' created.");
            } else {
                sportsCategory = existingSportsCategory.get();
            }

            Optional<Category> existingElectronicsCategory = categoryRepository.findByName("Electronics");
            Category electronicsCategory;
            if (existingElectronicsCategory.isEmpty()) {
                electronicsCategory = new Category("Electronics");
                electronicsCategory = categoryRepository.save(electronicsCategory);
                System.out.println("Category 'Electronics' created.");
            } else {
                electronicsCategory = existingElectronicsCategory.get();
            }


            // 6. Create Products with the correct constructor and associated Seller/Category objects
            // Ensure product names are unique if you have a unique constraint
            if (productRepository.findByName("Running Shoes").isEmpty()) {
                productRepository.save(new Product(
                    "Running Shoes",              // name
                    "High-performance running shoes for athletes.", // description
                    180.00,                      // price (changed from 18000.00, assuming currency like USD/EUR)
                    50,                          // stock (Integer)
                    sportyStore,            // Seller object
                    sportsCategory               // Category object
                ));
                System.out.println("Product 'Running Shoes' created.");
            }

            if (productRepository.findByName("Smart Watch").isEmpty()) {
                productRepository.save(new Product(
                    "Smart Watch",
                    "A versatile smart watch with health tracking features.",
                    250.00,
                    30,
                    sportyStore, // Assuming Sporty Gear also sells electronics, or create a new seller
                    electronicsCategory
                ));
                System.out.println("Product 'Smart Watch' created.");
            }
            // Add more products as needed
        };
    }
}
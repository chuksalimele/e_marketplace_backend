// CartRepository.java
package com.marketplace.emarketplacebackend.repository;

import com.marketplace.emarketplacebackend.model.Cart;
import com.marketplace.emarketplacebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Custom method to find a Cart by the User it belongs to
    // Spring Data JPA can automatically generate the query for this method name
    Optional<Cart> findByUser(User user);
}
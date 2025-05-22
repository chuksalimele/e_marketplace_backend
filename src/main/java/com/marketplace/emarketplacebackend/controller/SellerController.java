// SellerController.java (CORRECTED createSeller method)
package com.marketplace.emarketplacebackend.controller;

import com.marketplace.emarketplacebackend.model.Seller;
import com.marketplace.emarketplacebackend.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) // Adjust for Flutter app's port
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    SellerRepository sellerRepository;

    // Get all sellers - accessible by any authenticated user
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    // Get seller by ID - accessible by any authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Optional<Seller> sellerData = sellerRepository.findById(id);
        return sellerData.map(seller -> new ResponseEntity<>(seller, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new seller - Only for ADMINS
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        try {
            // CORRECTED LINE: Directly save the 'seller' object received in the request body.
            // Spring will handle generating the ID if 'seller.id' is null.
            Seller _seller = sellerRepository.save(seller);
            return new ResponseEntity<>(_seller, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging purposes in a real application
            // logger.error("Error creating seller: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing seller - Only for ADMINS
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller seller) {
        Optional<Seller> sellerData = sellerRepository.findById(id);

        if (sellerData.isPresent()) {
            Seller _seller = sellerData.get();
            // Assuming the 'seller' object from @RequestBody contains the updated fields
            _seller.setName(seller.getName());
            _seller.setLocation(seller.getLocation());
            _seller.setRating(seller.getRating());
            _seller.setProfileImageUrl(seller.getProfileImageUrl());
            _seller.setDescription(seller.getDescription());
            _seller.setCategories(seller.getCategories());
            return new ResponseEntity<>(sellerRepository.save(_seller), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a seller - Only for ADMINS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteSeller(@PathVariable Long id) {
        try {
            sellerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
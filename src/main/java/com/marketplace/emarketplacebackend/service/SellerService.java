// SellerService.java
package com.marketplace.emarketplacebackend.service;

import com.marketplace.emarketplacebackend.model.Seller;
import com.marketplace.emarketplacebackend.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    // --- CRUD Operations ---

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}
package com.stock.inventory.repository;

import com.stock.inventory.repository.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    List<Supplier> findByNameContainingIgnoreCase(String name);
    
    Optional<Supplier> findByIdentificationDocument(String identificationDocument);
    
    Optional<Supplier> findByEmail(String email);
    
    boolean existsByIdentificationDocument(String identificationDocument);
}
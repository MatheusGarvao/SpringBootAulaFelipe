package com.stock.inventory.repository;

import com.stock.inventory.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    Optional<Product> findByCode(String code);
    
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findBySupplierId(Long supplierId);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :minQuantity")
    List<Product> findProductsWithLowStock(@Param("minQuantity") Integer minQuantity);
}
package com.stock.inventory.repository;

import com.stock.inventory.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
}

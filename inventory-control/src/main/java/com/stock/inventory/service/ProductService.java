package com.stock.inventory.service;

import com.stock.inventory.repository.ProductRepository;
import com.stock.inventory.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCode(productDetails.getCode());
        product.setPurchasePrice(productDetails.getPurchasePrice());
        product.setSalePrice(productDetails.getSalePrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setCategory(productDetails.getCategory());
        product.setSupplier(productDetails.getSupplier());
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsWithLowStock(Integer minQuantity) {
        return productRepository.findProductsWithLowStock(minQuantity);
    }
}
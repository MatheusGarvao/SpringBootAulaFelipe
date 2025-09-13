package com.stock.inventory.service;

import com.stock.inventory.repository.ProductRepository;
import com.stock.inventory.repository.entity.Category;
import com.stock.inventory.repository.entity.Product;
import com.stock.inventory.repository.entity.Supplier;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository,
                          CategoryService categoryService,
                          SupplierService supplierService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product patch) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        String[] alwaysIgnore = new String[] { "id", "category", "supplier" };

        String[] ignore = Stream.concat(
                Arrays.stream(getNullPropertyNames(patch)),
                Arrays.stream(alwaysIgnore)
        ).toArray(String[]::new);

        BeanUtils.copyProperties(patch, product, ignore);

        if (patch.getCategory() != null) {
            Long catId = patch.getCategory().getId();
            if (catId == null) {
                throw new IllegalArgumentException("category.id é obrigatório quando enviar 'category'.");
            }
            Category ref = categoryService.refOrNotFound(catId);
            product.setCategory(ref);
        }

        if (patch.getSupplier() != null) {
            Long supId = patch.getSupplier().getId();
            if (supId == null) {
                throw new IllegalArgumentException("supplier.id é obrigatório quando enviar 'supplier'.");
            }
            Supplier ref = supplierService.refOrNotFound(supId);
            product.setSupplier(ref);
        }

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

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}

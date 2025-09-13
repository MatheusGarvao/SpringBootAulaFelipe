package com.stock.inventory.service;

import com.stock.inventory.repository.CategoryRepository;
import com.stock.inventory.repository.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category patch) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (patch.getName() != null) {
            String newName = patch.getName();
            String currentName = category.getName();
            if (!newName.equals(currentName) && categoryRepository.existsByName(newName)) {
                throw new RuntimeException("Category with name '" + newName + "' already exists");
            }
        }

        final BeanWrapper src = new BeanWrapperImpl(patch);
        String[] nullProps = Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);

        String[] ignore = Stream.concat(
                Arrays.stream(nullProps),
                Arrays.stream(new String[] { "id" })
        ).toArray(String[]::new);

        BeanUtils.copyProperties(patch, category, ignore);

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Category refOrNotFound(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada com o ID: " + id);
        }
        return categoryRepository.getReferenceById(id);
    }
}
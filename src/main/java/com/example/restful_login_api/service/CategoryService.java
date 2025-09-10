package com.example.restful_login_api.service;

import com.example.restful_login_api.domain.Category;
import com.example.restful_login_api.dto.category.CategoryResponseDTO;
import com.example.restful_login_api.dto.category.CreateCategoryDTO;
import com.example.restful_login_api.dto.category.UpdateCategoryDTO;
import com.example.restful_login_api.infra.exception.ResourceNotFoundException;
import com.example.restful_login_api.repository.CategoryRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No category found with id: " + id));
    }

    public List<CategoryResponseDTO> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Category> categories = this.categoryRepository.findAll(pageable).getContent();
        return categories.stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    public Category createCategory(CreateCategoryDTO category) {
        Category newCategory = new Category();
        newCategory.setName(category.name());
        return categoryRepository.save(newCategory);
    }

    public Category updateCategory(UpdateCategoryDTO category) {
        Category existingCategory = categoryRepository.findById(category.id())
                .orElseThrow(() -> new ResourceNotFoundException("No category found with id: " + category.id()));
        existingCategory.setName(category.name());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No category found with id: " + id));
        categoryRepository.delete(category);
    }
}

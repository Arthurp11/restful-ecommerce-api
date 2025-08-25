package com.example.restful_login_api.repository;

import com.example.restful_login_api.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
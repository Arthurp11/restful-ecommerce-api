package com.example.restful_login_api.repository;

import com.example.restful_login_api.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}

package com.example.restful_login_api.dto.product;

import com.example.restful_login_api.domain.category.Category;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id,
                                 String name,
                                 String description,
                                 String brand,
                                 BigDecimal price,
                                 Integer inventory,
                                 Category category) {}
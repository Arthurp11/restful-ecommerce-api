package com.example.restful_login_api.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(Long id,
                                 String name,
                                 String description,
                                 String brand,
                                 BigDecimal price,
                                 Integer inventory,
                                 String categoryName,
                                 List<String> images) {}
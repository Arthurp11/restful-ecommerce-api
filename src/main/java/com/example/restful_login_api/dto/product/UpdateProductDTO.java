package com.example.restful_login_api.dto.product;
import com.example.restful_login_api.domain.Category;
import com.example.restful_login_api.domain.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateProductDTO(
        Long id,
        String name,
        String description,
        String brand,
        BigDecimal price,
        Integer inventory,
        Category category,
        List<Image> images
) {}

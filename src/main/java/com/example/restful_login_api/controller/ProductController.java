package com.example.restful_login_api.controller;

import com.example.restful_login_api.domain.product.Product;
import com.example.restful_login_api.dto.product.ProductResponseDTO;
import com.example.restful_login_api.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(name = "/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductResponseDTO> products = productService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }
}

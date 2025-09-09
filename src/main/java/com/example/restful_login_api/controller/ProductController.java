package com.example.restful_login_api.controller;

import com.example.restful_login_api.domain.Product;
import com.example.restful_login_api.dto.product.CreateProductDTO;
import com.example.restful_login_api.dto.product.ProductResponseDTO;
import com.example.restful_login_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductResponseDTO> products = productService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDTO product) {
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.ok(newProduct);
    }
}

package com.example.restful_login_api.service.product;

import com.example.restful_login_api.domain.category.Category;
import com.example.restful_login_api.domain.product.Product;
import com.example.restful_login_api.dto.product.CreateProductDTO;
import com.example.restful_login_api.dto.product.ProductResponseDTO;
import com.example.restful_login_api.infra.exception.ResourceNotFoundException;
import com.example.restful_login_api.repository.CategoryRepository;
import com.example.restful_login_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

    }

    public List<ProductResponseDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = this.productRepository.findAll(pageable);
        return productsPage.map(product -> new ProductResponseDTO(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getBrand(),
        product.getPrice(),
        product.getInventory(),
        product.getCategory()
        )).stream().toList();
    }

    public Product createProduct(CreateProductDTO product) {
        Category category = categoryRepository.findById(product.category().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + product.category().getId()));

        Product newProduct = new Product();
        newProduct.setName(product.name());
        newProduct.setDescription(product.description());
        newProduct.setBrand(product.brand());
        newProduct.setPrice(product.price());
        newProduct.setInventory(product.inventory());
        newProduct.setCategory(category);

        return productRepository.save(newProduct);
    }

    public Product updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setInventory(product.getInventory());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImages(product.getImages());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        productRepository.delete(existingProduct);
    }
}

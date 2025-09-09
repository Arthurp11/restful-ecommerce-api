package com.example.restful_login_api.service;

import com.example.restful_login_api.domain.Category;
import com.example.restful_login_api.domain.Image;
import com.example.restful_login_api.domain.Product;
import com.example.restful_login_api.dto.product.CreateProductDTO;
import com.example.restful_login_api.dto.product.ProductResponseDTO;
import com.example.restful_login_api.dto.product.UpdateProductDTO;
import com.example.restful_login_api.infra.exception.ResourceNotFoundException;
import com.example.restful_login_api.repository.CategoryRepository;
import com.example.restful_login_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Product> products = this.productRepository.findAll(pageable).getContent();
        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getBrand(),
                        product.getPrice(),
                        product.getInventory(),
                        product.getCategory().getName(),
                        product.getImages().stream()
                                .map(Image::getDownloadUrl)
                                .toList()
                ))
                .toList();
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
        newProduct.setImages(product.images().stream()
                .map(imgUrl -> {
                    Image img = new Image();
                    img.setDownloadUrl(String.valueOf(imgUrl));
                    img.setProduct(newProduct);
                    return img;
                })
                .toList());

        return productRepository.save(newProduct);
    }

    public Product updateProduct(UpdateProductDTO product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        existingProduct.setName(product.name());
        existingProduct.setDescription(product.description());
        existingProduct.setBrand(product.brand());
        existingProduct.setPrice(product.price());
        existingProduct.setInventory(product.inventory());
        existingProduct.setCategory(product.category());
        existingProduct.setImages(product.images().stream()
                .map(imgUrl -> {
                    Image img = new Image();
                    img.setDownloadUrl(String.valueOf(imgUrl));
                    img.setProduct(existingProduct);
                    return img;
                })
                .toList());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        productRepository.delete(existingProduct);
    }
}

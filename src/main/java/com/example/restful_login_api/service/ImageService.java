package com.example.restful_login_api.service;

import com.example.restful_login_api.domain.Image;
import com.example.restful_login_api.domain.Product;
import com.example.restful_login_api.infra.exception.ResourceNotFoundException;
import com.example.restful_login_api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    ImageRepository repository;

    @Autowired
    ProductService productService;

    public Image getImageById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    public List<Image> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<Image> image = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image newImage = new Image();
                newImage.setFileName(file.getOriginalFilename());
                newImage.setFileType(file.getContentType());
                newImage.setImage(new SerialBlob(file.getBytes()));
                newImage.setProduct(product);

                String downloadUrl = "/api/images/" + newImage.getId();
                newImage.setDownloadUrl(downloadUrl);

                Image savedImage = repository.save(newImage);

                savedImage.setDownloadUrl("/api/images/" + savedImage.getId());
                repository.save(savedImage);

                image.add(savedImage);
            }   catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to update image", e);
            }
        }

        return image;
    }

    public Image updateImage(MultipartFile file, Long id) {
        Image image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            repository.save(image);
        }
        catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image", e);
        }

        return repository.save(image);
    }

    public void deleteImage(Long id) {
        Image image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
        repository.deleteById(id);
    }
}

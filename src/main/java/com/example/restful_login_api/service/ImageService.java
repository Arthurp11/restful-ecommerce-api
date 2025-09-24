package com.example.restful_login_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.restful_login_api.domain.Image;
import com.example.restful_login_api.domain.Product;
import com.example.restful_login_api.infra.exception.ResourceNotFoundException;
import com.example.restful_login_api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    ImageRepository repository;

    @Autowired
    ProductService productService;

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String awsBucketName;

    public Image getImageById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    public List<Image> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<Image> image = new ArrayList<>();

        for (MultipartFile file : files) {
            Image newImage = new Image();
            newImage.setFileName(file.getOriginalFilename());
            newImage.setFileType(file.getContentType());
            newImage.setDownloadUrl(this.uploadImgToS3(file));
            newImage.setProduct(product);
            image.add(repository.save(newImage));
        }

        return image;
    }

    public Image updateImage(MultipartFile file, Long id) {
        Image image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setDownloadUrl(this.uploadImgToS3(file));

        repository.save(image);

        return repository.save(image);
    }

    public void deleteImage(Long id) {
        Image image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
        repository.deleteById(id);
    }

    private String uploadImgToS3(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = this.convertMultiPartToFile(multipartFile);
            s3Client.putObject(awsBucketName, fileName, file);
            file.delete();
            return s3Client.getUrl(awsBucketName, fileName).toString();
        } catch (Exception e) {
            System.out.println("Error uploading file to S3: " + e.getMessage());
            return null;
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}

package edu.neu.csye6225.csye6225fall2024.controller;

import edu.neu.csye6225.csye6225fall2024.model.ImageMetadata;
import edu.neu.csye6225.csye6225fall2024.repository.ImageMetadataRepository;
import edu.neu.csye6225.csye6225fall2024.service.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("v1/user/self/pic")
public class ImageController {

    @Autowired
    private S3BucketService s3Service;

    @Autowired
    private ImageMetadataRepository imageMetadataRepository;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws IOException {
        try {
            String fileUrl = s3Service.uploadFile(file);

            ImageMetadata metadata = new ImageMetadata();
            metadata.setId(userId);
            metadata.setFileName(file.getOriginalFilename());
            metadata.setUrl(fileUrl);
            metadata.setUploadDate(LocalDate.now());
            metadata.setUserId(userId);

            imageMetadataRepository.save(metadata);

            return ResponseEntity.status(HttpStatus.CREATED).body(metadata);
        } catch (IOException e) {
            // bad request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to upload image.");
        }
    }
}

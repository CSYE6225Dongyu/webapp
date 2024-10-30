package edu.neu.csye6225.csye6225fall2024.service;

import edu.neu.csye6225.csye6225fall2024.model.ImageMetadata;
import edu.neu.csye6225.csye6225fall2024.repository.ImageMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ImageMetadataService {

    @Autowired
    private ImageMetadataRepository imageMetadataRepository;

    @Autowired
    private S3BucketService s3BucketService;

    public ImageMetadata insertMetadata(String userId, String fileUUID, String fileName, String fileUrl) {
        ImageMetadata metadata = new ImageMetadata();

        metadata.setId(fileUUID);
        metadata.setFileName(fileName + fileUUID);
        metadata.setUrl(fileUrl);
        metadata.setUploadDate(LocalDateTime.now(ZoneOffset.UTC));
        metadata.setUserId(userId);
        imageMetadataRepository.save(metadata);
        // print metadata
        return metadata;
    }

    // Retrieve all images created by the specified user
    public List<ImageMetadata> getUserImages(String userId) {
        return imageMetadataRepository.findByUserId(userId);
    }


    public void deleteUserImages(String userId) {
        List<ImageMetadata> images = imageMetadataRepository.findByUserId(userId);

        if (images.isEmpty()) {
            throw new IllegalArgumentException("No images found for the user.");
        }

        for (ImageMetadata image : images) {
            if (!image.getUserId().equals(userId)) {
                throw new SecurityException("Unauthorized to delete images of another user.");
            }
            s3BucketService.deleteFile(image.getFileName());
            imageMetadataRepository.delete(image);
        }
    }


}

package edu.neu.csye6225.csye6225fall2024.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

@Service
public class S3BucketService {

    private final S3Client s3Client;

    // S3 bucket name
    private final String bucketName = "your-s3-bucket-name";

    public S3BucketService() {
        // Initialize S3 client with region and default credentials provider
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)  // Replace with your bucket's region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    /**
     * Upload a file to S3 bucket and return the file URL
     * @param file MultipartFile to be uploaded
     * @return URL of the uploaded file
     * @throws IOException if file cannot be read
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName =  file.getOriginalFilename();

        // Construct the S3 PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // Upload the file
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Generate and return the URL of the uploaded file
        URL fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName));
        return fileUrl.toString();
    }

    /**
     * Delete a file from S3 bucket
     * @param fileName Name of the file to delete
     */
    public void deleteFile(String fileName) {
        // Construct the S3 DeleteObjectRequest
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        // Delete the file
        s3Client.deleteObject(deleteObjectRequest);
    }

}

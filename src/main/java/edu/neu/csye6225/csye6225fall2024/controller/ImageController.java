package edu.neu.csye6225.csye6225fall2024.controller;

import edu.neu.csye6225.csye6225fall2024.dto.UserGETDTO;
import edu.neu.csye6225.csye6225fall2024.model.ImageMetadata;
import edu.neu.csye6225.csye6225fall2024.service.ImageMetadataService;
import edu.neu.csye6225.csye6225fall2024.service.S3BucketService;
import edu.neu.csye6225.csye6225fall2024.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/user/self/pic")
public class ImageController {

    @Autowired
    private S3BucketService s3Service;

    @Autowired
    private UserService userService;

    @Autowired
     private ImageMetadataService imageMetadataService;

    //get ID form SecurityContextHolder
    private String getCurrentUserId() {
        //get email form SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserGETDTO user = userService.getUserByEmail(email);
        return user.getId();
    }



    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws MultipartException {

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/png") &&
                        !contentType.equals("image/jpeg") &&
                        !contentType.equals("image/jpg"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only PNG, JPG, and JPEG image files are allowed.");
        }

        try {
            String userId = getCurrentUserId();

            String fileName = file.getOriginalFilename();

            String fileUUID = UUID.randomUUID().toString();

            String fileUrl = s3Service.uploadFile(file, fileUUID);

            ImageMetadata metadata = imageMetadataService.insertMetadata(userId, fileUUID, fileName, fileUrl);

            return ResponseEntity.status(HttpStatus.CREATED).body(metadata);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getImagesByUserId() {
        String userId = getCurrentUserId();

        List<ImageMetadata> images = imageMetadataService.getUserImages(userId);

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No images found for the user.");
        }


        return ResponseEntity.ok(images);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUserImages() {
        try {
            String userId = getCurrentUserId();
            imageMetadataService.deleteUserImages(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("All images deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.OPTIONS,RequestMethod.HEAD,RequestMethod.PATCH,RequestMethod.TRACE})
    public ResponseEntity<String> invalidMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .build();
    }

}

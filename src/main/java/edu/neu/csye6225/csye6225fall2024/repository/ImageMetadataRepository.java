package edu.neu.csye6225.csye6225fall2024.repository;

import edu.neu.csye6225.csye6225fall2024.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, String> {
    Optional<ImageMetadata> findByUserId(String userId);
}

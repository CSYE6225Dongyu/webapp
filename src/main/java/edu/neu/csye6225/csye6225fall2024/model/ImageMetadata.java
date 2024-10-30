package edu.neu.csye6225.csye6225fall2024.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "images_metadata")
public class ImageMetadata {

    @Id
    private  String id;
    private  String fileName;
    private String url;
    private LocalDate uploadDate;
    private String userId;

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

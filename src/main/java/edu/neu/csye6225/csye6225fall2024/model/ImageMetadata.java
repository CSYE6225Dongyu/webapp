package edu.neu.csye6225.csye6225fall2024.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "images_metadata")
public class ImageMetadata {

    @Id
    private  String id;

    @Column(name = "user_id")
    private String userId;// find by userID

    private  String fileName;
    private String url;
    private LocalDateTime uploadDate;


    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getUploadDate() {
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

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package edu.neu.csye6225.csye6225fall2024.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
public class EmailVerification {

    @Id
    private  String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at") // set to null when generate
    private LocalDateTime expiresAt;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

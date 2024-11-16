package edu.neu.csye6225.csye6225fall2024.controller;

import edu.neu.csye6225.csye6225fall2024.service.UserVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
public class VerificationController {

    private final UserVerificationService userVerificationService;

    public VerificationController(UserVerificationService userVerificationService) {
        this.userVerificationService = userVerificationService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<String> verifyUser(@PathVariable String token) {
        try {
            boolean isVerified = userVerificationService.verifyUser(token);

            if (isVerified) {
                return ResponseEntity.ok("User verified successfully!");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during verification.");
        }
    }
}

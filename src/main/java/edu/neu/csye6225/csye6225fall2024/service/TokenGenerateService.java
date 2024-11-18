package edu.neu.csye6225.csye6225fall2024.service;

import edu.neu.csye6225.csye6225fall2024.model.EmailVerification;
import edu.neu.csye6225.csye6225fall2024.repository.EmailVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenGenerateService {
    private final EmailVerificationRepository emailVerificationRepository;

    public TokenGenerateService(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    /**
     * Generate token and store
    */
    public String generateAndStoreToken(String email, String userId) {
        String token = UUID.randomUUID().toString();

        LocalDateTime expiresAt = LocalDateTime.now();

        Optional<EmailVerification> existingRecord = emailVerificationRepository.findByUserId(userId);

        if(existingRecord.isPresent()) {
            //already exit, create a new one
            EmailVerification record = existingRecord.get();
//            record.setUserId(userId);
            record.setToken(token);
            record.setExpiresAt(null);
            emailVerificationRepository.save(record);
        } else {
            // create a new one
            EmailVerification newRecord = new EmailVerification();
            newRecord.setId(UUID.randomUUID().toString());
            newRecord.setUserId(userId);
            newRecord.setToken(token);
            newRecord.setEmail(email);
            newRecord.setExpiresAt(null);
            emailVerificationRepository.save(newRecord);
        }
        return token;
    }

    /**
     * update when email is send
     * @param email
     * @param sentAt
     */
    public void updateTokenExpiry(String email, LocalDateTime sentAt) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Token not found for user: " + email));
        emailVerification.setExpiresAt(sentAt.plusMinutes(2)); // 2 min to expire
        emailVerificationRepository.save(emailVerification);
    }


}

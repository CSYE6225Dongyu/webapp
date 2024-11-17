package edu.neu.csye6225.csye6225fall2024.service;

import edu.neu.csye6225.csye6225fall2024.model.EmailVerification;
import edu.neu.csye6225.csye6225fall2024.repository.EmailVerificationRepository;
import edu.neu.csye6225.csye6225fall2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserVerificationService {

//Not use autowired
//    private final EmailVerificationRepository emailVerificationRepository;
//    private final UserRepository userRepository;
//
//    public UserVerificationService(EmailVerificationRepository emailVerificationRepository,
//                                   UserRepository userRepository) {
//        this.emailVerificationRepository = emailVerificationRepository;
//        this.userRepository = userRepository;
//    }


    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean verifyUser(String token) {
        // select the token table and check token that not expired
        Optional<EmailVerification> verificationOpt =
                emailVerificationRepository.findByTokenAndExpiresAtAfter(token, LocalDateTime.now());

        if (verificationOpt.isPresent()) {
            EmailVerification verification = verificationOpt.get();

            // update users table isVerified filed
            userRepository.updateIsVerified(verification.getUserId(), true);

            // delete the token registry
            emailVerificationRepository.delete(verification);

            return true;
        }

        return false; // No validate token found
    }
}

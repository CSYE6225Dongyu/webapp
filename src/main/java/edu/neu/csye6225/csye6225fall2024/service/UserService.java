package edu.neu.csye6225.csye6225fall2024.service;

import edu.neu.csye6225.csye6225fall2024.dto.UserGETDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserPostDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserUpdateDTO;
import edu.neu.csye6225.csye6225fall2024.model.UserModel;
import edu.neu.csye6225.csye6225fall2024.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private MeterRegistry meterRegistry;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SnsService snsService;
    private final TokenGenerateService tokenService;

    // constructor inject dependency
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SnsService snsService, TokenGenerateService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.snsService = snsService;
        this.tokenService = tokenService;
    }


    //create user
    public void createUser(UserPostDTO userPostDTO) {
        Timer.Sample sample = Timer.start(meterRegistry);
        //check the user

        if(userPostDTO.getEmail() == null || userPostDTO.getPassword() == null || userPostDTO.getLastName() == null)
            throw new IllegalArgumentException("missing filed required");

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(userPostDTO.getEmail());
        // bycrypt
        user.setPassword(passwordEncoder.encode(userPostDTO.getPassword()));

        user.setFirstName(userPostDTO.getFirstName());
        user.setLastName(userPostDTO.getLastName());

        user.setAccountCreated(LocalDateTime.now());
        user.setAccountUpdated(LocalDateTime.now());


        System.out.println("user id: " +  user.getId());
        System.out.println("user id: " +  user.getEmail());

        // save to database
        userRepository.save(user);
        sample.stop(meterRegistry.timer("db.userdata.insert.timer", "operation", "createUser"));
    }

    public void updateUser(String email, UserUpdateDTO userUpdateDTO) {
        Timer.Sample sample = Timer.start(meterRegistry);
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // when called, must change something
        boolean updated = false;
        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
            updated = true;
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
            updated = true;
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
            updated = true;
        }
        // exception when nothing changes
        if (!updated)
            throw new IllegalArgumentException("No valid fields to update");

        user.setAccountUpdated(LocalDateTime.now());

        userRepository.save(user);
        sample.stop(meterRegistry.timer("db.userdata.update.timer", "operation", "updateUser"));
    }

    public UserGETDTO getUserByEmail(String email) {
        Timer.Sample sample = Timer.start(meterRegistry);
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        sample.stop(meterRegistry.timer("db.userdata.find.timer", "operation", "getUserByEmail"));
        return new UserGETDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAccountCreated(),
                user.getAccountUpdated()
        );
    }
    /*
   SNS and token service handler
     */

    /**
     * Generate token and send SNS
     * update expired time after sned Validation function return
     * @param userPostDTO
     */
    public void handleUserCreation(UserPostDTO userPostDTO) {
        // check if user exit
        Optional<UserModel> existingUserOpt = userRepository.findByEmail(userPostDTO.getEmail());

        if (existingUserOpt.isPresent()) {
            UserModel existingUser = existingUserOpt.get();

            if (existingUser.getIsVerified()) {
                throw new IllegalArgumentException("User email is already validated.");
            } else {
                // if not verified send SNS message
                String token = tokenService.generateAndStoreToken(existingUser.getEmail(), existingUser.getId());
                sendValidationMessage(existingUser.getEmail(), token);
                throw new IllegalArgumentException("User is created, need validation.");
            }
        }

        // if no user, create the user
        createUser(userPostDTO);

        // send SNS
        Optional<UserModel> newUserOpt = userRepository.findByEmail(userPostDTO.getEmail());
        newUserOpt.ifPresent(newUser -> {
            String token = tokenService.generateAndStoreToken(newUser.getEmail(), newUser.getId());
            sendValidationMessage( newUser.getEmail(), token);
        });
    }

    private void sendValidationMessage(String email, String token) {
        String message = String.format("{\"email\": \"%s\", \"token\": \"%s\"}", email, token);
        String messageId = snsService.publishEvent(email, message);


        if (messageId == null || messageId.isEmpty()) {
            throw new IllegalStateException("SNS message publishing failed: no messageId returned");
        }

        System.out.println("Validation message sent successfully. Message ID: " + messageId);
    }
}

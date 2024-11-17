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

    // constructor inject dependency
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SnsService snsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.snsService = snsService;
    }

    // send SNS and handle Multiple create
    public void handleUserCreation(UserPostDTO userPostDTO) {
        // check if user exit
        Optional<UserModel> existingUserOpt = userRepository.findByEmail(userPostDTO.getEmail());

        if (existingUserOpt.isPresent()) {
            UserModel existingUser = existingUserOpt.get();

            if (existingUser.getIsVerified()) {
                throw new IllegalArgumentException("User email is already validated.");
            } else {
                // if not verified send SNS message
                sendValidationMessage(existingUser.getId(), existingUser.getEmail());
                return;
            }
        }

        // if no user, create the user
        createUser(userPostDTO);

        // send SNS
        Optional<UserModel> newUserOpt = userRepository.findByEmail(userPostDTO.getEmail());
        newUserOpt.ifPresent(newUser -> sendValidationMessage(newUser.getId(), newUser.getEmail()));
    }

    private void sendValidationMessage(String userId, String email) {
        String message = String.format("{\"userId\": \"%s\", \"email\": \"%s\"}", userId, email);
        snsService.publishEvent(message);
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
}

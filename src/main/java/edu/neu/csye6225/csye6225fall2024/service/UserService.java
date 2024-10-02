package edu.neu.csye6225.csye6225fall2024.service;

import edu.neu.csye6225.csye6225fall2024.dto.UserDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserGETDTO;
import edu.neu.csye6225.csye6225fall2024.model.UserModel;
import edu.neu.csye6225.csye6225fall2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;// password class from AuthorizeConfig

    //create user
    public void createUser(UserDTO userDTO) {
        //check the user
        Optional<UserModel> existUser = userRepository.findByEmail(userDTO.getEmail());
        if (existUser.isPresent()) {
            throw new IllegalArgumentException("User email already exists.");
        }

        UserModel user = new UserModel();
        user.setEmail(userDTO.getEmail());
        // bycrypt
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        user.setAccountCreated(LocalDateTime.now());
        user.setAccountUpdated(LocalDateTime.now());

        // save to database
        userRepository.save(user);
    }

    public void updateUser(String email, UserDTO userDTO) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setAccountUpdated(LocalDateTime.now());

        userRepository.save(user);
    }

    public UserGETDTO getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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

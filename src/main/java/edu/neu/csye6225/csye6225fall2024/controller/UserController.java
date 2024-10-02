package edu.neu.csye6225.csye6225fall2024.controller;

import edu.neu.csye6225.csye6225fall2024.dto.UserDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserGETDTO;
import edu.neu.csye6225.csye6225fall2024.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edu.neu.csye6225.csye6225fall2024.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> creatUser(@RequestBody UserDTO userDTO) {
        try {
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while creating user");
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserGETDTO> getUser() {
        try {
            String email = getCurrentUserEmail();
            UserGETDTO user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/self")
    public ResponseEntity<String> updateUser( @RequestBody UserDTO userDTO) {
        try {
            String email = getCurrentUserEmail();
            userService.updateUser(email, userDTO);
            return ResponseEntity.ok("User updated success");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while updating user");
        }
    }

    //get emial form SecurityContextHolder
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

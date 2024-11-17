package edu.neu.csye6225.csye6225fall2024.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neu.csye6225.csye6225fall2024.dto.UserGETDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserPostDTO;
import edu.neu.csye6225.csye6225fall2024.dto.UserUpdateDTO;
import edu.neu.csye6225.csye6225fall2024.service.UserService;
import edu.neu.csye6225.csye6225fall2024.service.ValidateFields;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private MeterRegistry meterRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidateFields validateFields;

    @PostMapping
    public ResponseEntity<UserGETDTO> createUser(@Valid @RequestBody UserPostDTO userPostDTO) {
        meterRegistry.counter("api.v1.user.post.count").increment();
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            // create user
            userService.handleUserCreation(userPostDTO);
            // return info
            UserGETDTO userGETDTO = userService.getUserByEmail(userPostDTO.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(userGETDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } finally {
            sample.stop(meterRegistry.timer("api.v1.user.post.timer"));
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserGETDTO> getUser() {
        meterRegistry.counter("api.v1.user.self.get.count").increment();
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            String email = getCurrentUserEmail();
            UserGETDTO user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } finally {
            sample.stop(meterRegistry.timer("api.v1.user.self.get.timer"));
        }
    }

    @PutMapping("/self")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> requestBody) {
        meterRegistry.counter("api.v1.user.self.put.count").increment();
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Validate fields only once
            String invalidField = validateFields.updateValidate(requestBody);
            if (invalidField != null) {
                return ResponseEntity.badRequest().body("Unexpected field: " + invalidField);
            }

            // Convert requestBody to UserUpdateDTO
            UserUpdateDTO userUpdateDTO = new ObjectMapper().convertValue(requestBody, UserUpdateDTO.class);

            // Get the current user's email
            String email = getCurrentUserEmail();

            // Update user information
            userService.updateUser(email, userUpdateDTO);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating user");
        } finally {
            // stop timer
            sample.stop(meterRegistry.timer("api.v1.user.self.put.timer"));
        }
    }


    //get emial form SecurityContextHolder
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE,RequestMethod.OPTIONS,RequestMethod.HEAD,RequestMethod.PATCH,RequestMethod.TRACE})
    public ResponseEntity<String> invalidMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .build();
    }

    @RequestMapping(value = "/self", method = {RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.TRACE, RequestMethod.POST})
    public ResponseEntity<String> invalidSelfMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .build();
    }


}

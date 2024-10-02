package edu.neu.csye6225.csye6225fall2024.repository;

import edu.neu.csye6225.csye6225fall2024.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

package edu.neu.csye6225.csye6225fall2024.repository;

import edu.neu.csye6225.csye6225fall2024.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.isVerified = :isVerified WHERE u.id = :userId")
    void updateIsVerified(String userId, boolean isVerified);
}

// src/main/java/com/example/cubesat/repository/PasswordResetTokenRepository.java
package com.example.cubesat.repository;

import com.example.cubesat.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserId(Long userId);
}

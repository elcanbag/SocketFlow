package com.example.cubesat.service;

import com.example.cubesat.model.PasswordResetToken;
import com.example.cubesat.model.User;
import com.example.cubesat.repository.PasswordResetTokenRepository;
import com.example.cubesat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${app.reset-token.expiration-minutes}")
    private long expirationMinutes;

    public String registerUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return "Error: Username already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        return "User registered successfully";
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account Verification");
        message.setText("Your verification code is: " + code);
        mailSender.send(message);
    }

    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        tokenRepository.deleteByUserId(user.getId());
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(expirationMinutes));
        tokenRepository.save(resetToken);

        String resetUrl = "http://localhost/api/user/reset-password?token=" + token;
        SimpleMailMessage resetMessage = new SimpleMailMessage();
        resetMessage.setTo(user.getEmail());
        resetMessage.setSubject("Password Reset Request");
        resetMessage.setText(
                "Click the link below to reset your password:\n" +
                        resetUrl + "\n" +
                        "This link is valid for " + expirationMinutes + " minutes."
        );
        mailSender.send(resetMessage);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }
        User user = resetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}

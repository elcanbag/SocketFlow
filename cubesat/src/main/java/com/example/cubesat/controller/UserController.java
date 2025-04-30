package com.example.cubesat.controller;

import com.example.cubesat.model.User;
import com.example.cubesat.repository.UserRepository;
import com.example.cubesat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        String email    = req.get("email");

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Username already exists"));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setVerified(false);

        // Doğrulama kodu oluşturup sakla
        String code = userService.generateVerificationCode();
        user.setVerificationCode(code);
        userRepository.save(user);

        // E-postayı gönder
        userService.sendVerificationEmail(email, code);

        return ResponseEntity.ok(Map.of(
                "message", "User registered. Verification email sent"
        ));
    }

    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(
            @RequestParam String username,
            @RequestBody Map<String, String> payload) {

        String code = payload.get("code");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Verified"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Invalid code"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isEmpty() || !opt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
        if (!opt.get().isVerified()) {
            return ResponseEntity.status(403).body(Map.of("message", "Account not verified"));
        }

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }
}

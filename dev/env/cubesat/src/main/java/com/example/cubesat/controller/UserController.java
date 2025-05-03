package com.example.cubesat.controller;

import com.example.cubesat.model.CubeSat;
import com.example.cubesat.model.User;
import com.example.cubesat.repository.CubeSatRepository;
import com.example.cubesat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final CubeSatRepository cubeSatRepository;


    @PostMapping("/signup-with-cubesat")
    public ResponseEntity<?> signUpWithCubeSat(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String accessToken = request.get("accessToken");

        Optional<CubeSat> cubeSat = cubeSatRepository.findByAccessToken(accessToken);
        if (cubeSat.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid CubeSat token"));
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Username already exists"));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCubeSat(cubeSat.get());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }


    @PostMapping("/register-cubesat")
    public ResponseEntity<?> registerCubeSat(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String cubeSatName = request.get("cubeSatName");

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Username already exists"));
        }

        CubeSat cubeSat = new CubeSat();
        cubeSat.setName(cubeSatName);
        cubeSat.setAccessToken(UUID.randomUUID().toString());
        cubeSatRepository.save(cubeSat);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCubeSat(cubeSat);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "CubeSat registered successfully", "accessToken", cubeSat.getAccessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        String responseMessage = String.format("Login successful, username is \"%s\"", username);
        return ResponseEntity.ok(Map.of("message", responseMessage));
    }

}

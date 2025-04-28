package com.example.cubesat.controller;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.User;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(Authentication authentication, @RequestBody Map<String, String> request) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String deviceName = request.get("deviceName");

        Device device = new Device();
        device.setName(deviceName);
        device.setAccessToken(UUID.randomUUID().toString());
        device.setOwner(user);

        deviceRepository.save(device);

        return ResponseEntity.ok(Map.of("message", "Device registered successfully", "accessToken", device.getAccessToken()));
    }
}

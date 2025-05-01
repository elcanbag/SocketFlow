package com.example.cubesat.controller;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.User;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        return ResponseEntity.ok(Map.of(
                "message", "Device registered successfully",
                "accessToken", device.getAccessToken()
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDevices(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Map<String, Object>> devices = user.getDevices().stream()
                .map(device -> Map.<String, Object>of(
                        "id", device.getId(),
                        "name", device.getName(),
                        "accessToken", device.getAccessToken()
                ))
                .collect(Collectors.toList());


        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{token}/fields")
    public ResponseEntity<?> getDeviceFields(@PathVariable String token, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Device device = deviceRepository.findByAccessToken(token)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (!device.getOwner().equals(user)) {
            return ResponseEntity.status(403).body(Map.of("message", "Access denied"));
        }

        List<Map<String, Object>> fields = device.getFields().stream()
                .map(field -> Map.<String, Object>of(
                        "id", field.getId(),
                        "name", field.getName(),
                        "type", field.getType(),
                        "unit", field.getUnit()
                ))
                .collect(Collectors.toList());


        return ResponseEntity.ok(fields);
    }
}

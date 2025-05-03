package com.example.cubesat.controller;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.Field;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
public class FieldController {
    private final DeviceRepository deviceRepository;
    private final FieldRepository fieldRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addField(Authentication authentication, @RequestBody Map<String, String> request) {
        String token = request.get("token");
        String name = request.get("name");
        String type = request.get("type");
        String unit = request.get("unit");

        Device device = deviceRepository.findByAccessToken(token)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        Field field = new Field();
        field.setName(name);
        field.setType(type);
        field.setUnit(unit);
        field.setDevice(device);

        fieldRepository.save(field);

        return ResponseEntity.ok(Map.of("message", "Field created successfully"));
    }
}

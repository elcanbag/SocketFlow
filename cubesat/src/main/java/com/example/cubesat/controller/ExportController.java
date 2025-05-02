package com.example.cubesat.controller;

import com.example.cubesat.model.Device;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final DeviceRepository deviceRepository;
    private final ExportService exportService;

    @GetMapping
    public void exportData(
            HttpServletResponse response,
            @RequestParam String format,
            @RequestParam String accessToken,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Principal principal
    ) throws IOException {

        Device device = deviceRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"));


        if (!device.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }


        exportService.export(response, format, device.getId(), start, end);
    }
}

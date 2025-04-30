package com.example.cubesat.controller;

import com.example.cubesat.dto.FieldDTO;
import com.example.cubesat.dto.FieldRecordDTO;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.Field;
import com.example.cubesat.model.FieldRecord;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.FieldRecordRepository;
import com.example.cubesat.repository.FieldRepository;
import com.example.cubesat.service.FieldRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/devices/{deviceToken}")
@RequiredArgsConstructor
public class FieldRecordController {

    private final DeviceRepository deviceRepository;
    private final FieldRepository fieldRepository;
    private final FieldRecordRepository fieldRecordRepository;
    private final FieldRecordService fieldRecordService;

    @GetMapping("/fields")
    public ResponseEntity<List<FieldDTO>> getFields(@PathVariable String deviceToken) {
        Device device = deviceRepository.findByAccessToken(deviceToken)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        List<FieldDTO> fields = fieldRepository.findByDevice(device)
                .stream()
                .map(fieldRecordService::convertToFieldDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fields);
    }

    @GetMapping("/fields/latest")
    public ResponseEntity<List<FieldRecordDTO>> getLatestFieldRecords(@PathVariable String deviceToken) {
        Device device = deviceRepository.findByAccessToken(deviceToken)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        List<FieldRecordDTO> latestRecords = fieldRepository.findByDevice(device)
                .stream()
                .map(field -> fieldRecordRepository.findTopByFieldOrderByTimestampDesc(field).orElse(null))
                .filter(record -> record != null)
                .map(fieldRecordService::convertToFieldRecordDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(latestRecords);
    }

    @GetMapping("/fields/{fieldId}/records")
    public ResponseEntity<List<FieldRecordDTO>> getFieldRecords(
            @PathVariable String deviceToken,
            @PathVariable Long fieldId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        Device device = deviceRepository.findByAccessToken(deviceToken)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        Field field = fieldRepository.findById(fieldId)
                .filter(f -> f.getDevice().equals(device))
                .orElseThrow(() -> new RuntimeException("Field not found for this device"));

        List<FieldRecord> records;
        if (start != null && end != null) {
            records = fieldRecordRepository.findByFieldAndTimestampBetweenOrderByTimestampAsc(field, start, end);
        } else {
            records = fieldRecordRepository.findByFieldOrderByTimestampAsc(field);
        }

        List<FieldRecordDTO> recordDTOs = records.stream()
                .map(fieldRecordService::convertToFieldRecordDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recordDTOs);
    }

    @GetMapping("/fields/{fieldId}/records/latest")
    public ResponseEntity<FieldRecordDTO> getLatestFieldRecord(
            @PathVariable String deviceToken,
            @PathVariable Long fieldId
    ) {
        Device device = deviceRepository.findByAccessToken(deviceToken)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        Field field = fieldRepository.findById(fieldId)
                .filter(f -> f.getDevice().equals(device))
                .orElseThrow(() -> new RuntimeException("Field not found for this device"));

        FieldRecord latestRecord = fieldRecordRepository.findTopByFieldOrderByTimestampDesc(field)
                .orElseThrow(() -> new RuntimeException("No record found"));

        return ResponseEntity.ok(fieldRecordService.convertToFieldRecordDTO(latestRecord));
    }
}

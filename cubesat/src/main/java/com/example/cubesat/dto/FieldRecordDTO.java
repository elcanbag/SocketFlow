package com.example.cubesat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldRecordDTO {
    private Long id;
    private double dataValue;
    private LocalDateTime timestamp;
    private Long fieldId;
}

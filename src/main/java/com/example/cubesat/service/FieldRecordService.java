package com.example.cubesat.service;

import com.example.cubesat.dto.FieldDTO;
import com.example.cubesat.dto.FieldRecordDTO;

import com.example.cubesat.model.Field;
import com.example.cubesat.model.FieldRecord;
import org.springframework.stereotype.Service;

@Service
public class FieldRecordService {
    public FieldDTO convertToFieldDTO(Field field) {
        FieldDTO dto = new FieldDTO();
        dto.setId(field.getId());
        dto.setName(field.getName());
        dto.setType(field.getType());
        dto.setUnit(field.getUnit());
        return dto;
    }

    public FieldRecordDTO convertToFieldRecordDTO(FieldRecord record) {
        FieldRecordDTO dto = new FieldRecordDTO();
        dto.setId(record.getId());
        dto.setDataValue(record.getValue());
        dto.setTimestamp(record.getTimestamp());
        dto.setFieldId(record.getField().getId());
        return dto;
    }
}

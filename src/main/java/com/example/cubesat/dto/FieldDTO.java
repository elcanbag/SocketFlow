package com.example.cubesat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {
    private Long id;
    private String name;
    private String type;
    private String unit;


}

package com.example.cubesat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String unit;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}

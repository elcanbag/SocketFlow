package com.example.cubesat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "dates")
public class CubeSatRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recordDate;//
    private String temperature;
    private String humidity;
    private String x;
    private String y;
    private String z;
    private String lat;//
    private String longg;//
    private String internalTemp;
    private String pressure;
    private String alt;
    private String sat;//

    private LocalDateTime receivedAt;

    @ManyToOne
    @JoinColumn(name = "cubesat_id", nullable = false)
    private CubeSat cubeSat;
}

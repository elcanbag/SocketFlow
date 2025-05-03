package com.example.cubesat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ip;
    private String method;
    private String path;
    private String params;
    private LocalDateTime timestamp;
}

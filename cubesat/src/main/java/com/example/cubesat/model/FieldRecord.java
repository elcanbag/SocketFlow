package com.example.cubesat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FieldRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_value")
    private double value;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "field_id")
    @JsonIgnore
    private Field field;


}

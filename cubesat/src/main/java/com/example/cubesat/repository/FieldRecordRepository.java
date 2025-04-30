package com.example.cubesat.repository;

import com.example.cubesat.model.Field;
import com.example.cubesat.model.FieldRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FieldRecordRepository extends JpaRepository<FieldRecord, Long> {
    List<FieldRecord> findByFieldAndTimestampBetween(Field field, LocalDateTime start, LocalDateTime end);

    Optional<FieldRecord> findTopByFieldOrderByTimestampDesc(Field field);

    List<FieldRecord> findByFieldOrderByTimestampAsc(Field field);

    List<FieldRecord> findByFieldAndTimestampBetweenOrderByTimestampAsc(Field field, LocalDateTime start, LocalDateTime end);
}

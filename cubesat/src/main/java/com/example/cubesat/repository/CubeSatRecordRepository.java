package com.example.cubesat.repository;

import com.example.cubesat.model.CubeSat;
import com.example.cubesat.model.CubeSatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CubeSatRecordRepository extends JpaRepository<CubeSatRecord, Long> {
    CubeSatRecord findTopByCubeSatOrderByReceivedAtDesc(CubeSat cubeSat);


    List<CubeSatRecord> findByCubeSatAndReceivedAtBetween(CubeSat cubeSat, LocalDateTime start, LocalDateTime end);
}

package com.example.cubesat.service;

import com.example.cubesat.model.CubeSat;
import com.example.cubesat.model.CubeSatRecord;
import com.example.cubesat.repository.CubeSatRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CubeSatService {
    private final CubeSatRecordRepository repository;

    public void saveRecord(CubeSatRecord record) {
        repository.save(record);
    }

    public CubeSatRecord getLatestRecordForCubeSat(CubeSat cubeSat) {
        return repository.findTopByCubeSatOrderByReceivedAtDesc(cubeSat);
    }


    public List<CubeSatRecord> getRecordsBetweenDatesForCubeSat(CubeSat cubeSat, LocalDateTime start, LocalDateTime end) {
        return repository.findByCubeSatAndReceivedAtBetween(cubeSat, start, end);
    }
}

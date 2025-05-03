package com.example.cubesat.repository;

import com.example.cubesat.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByAccessToken(String accessToken);
}

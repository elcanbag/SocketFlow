package com.example.cubesat.repository;

import com.example.cubesat.model.CubeSat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CubeSatRepository extends JpaRepository<CubeSat, Long> {
    Optional<CubeSat> findByAccessToken(String accessToken);
}

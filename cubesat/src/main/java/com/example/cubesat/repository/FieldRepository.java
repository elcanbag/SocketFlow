package com.example.cubesat.repository;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    List<Field> findByDevice(Device device);
    Optional<Field> findByDeviceAndType(Device device, String type);
}

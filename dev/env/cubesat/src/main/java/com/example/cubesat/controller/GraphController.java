package com.example.cubesat.controller;

import com.example.cubesat.model.User;
import com.example.cubesat.repository.UserRepository;
import com.example.cubesat.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/graph")
@RequiredArgsConstructor
public class GraphController {
    private final GraphService graphService;
    private final UserRepository userRepository;

    @GetMapping("/cubesat-data/json")
    public ResponseEntity<Map<String, Object>> getCubeSatGraphData(
            Authentication authentication,
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate,
            @RequestParam("type") String type) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        Map<String, Object> graphData = graphService.getGraphDataForCubeSat(user.getCubeSat(), start, end, type);
        return ResponseEntity.ok(graphData);
    }
}

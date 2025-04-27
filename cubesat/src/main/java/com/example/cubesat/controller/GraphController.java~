package com.example.cubesat.controller;

import com.example.cubesat.service.GraphService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/graph")
@RequiredArgsConstructor
public class GraphController {
    private final GraphService graphService;

    @Operation(summary = "Get CubeSat Graph Data", description = "Returns JSON data for CubeSat graph visualization.")
    @GetMapping("/cubesat-data/json")
    public ResponseEntity<Map<String, Object>> getCubeSatGraphData(
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate,
            @RequestParam("type") String type) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        Map<String, Object> graphData = graphService.getGraphData(start, end, type);
        return ResponseEntity.ok(graphData);
    }
}

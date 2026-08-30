package com.spotpulse.backend.controller;

import com.spotpulse.backend.service.LocationSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationSearchService locationSearchService;

    public LocationController(LocationSearchService locationSearchService) {
        this.locationSearchService = locationSearchService;
    }

    // 내 주변 관광지 조회
    @GetMapping("/nearby")
    public List<Map<String, Object>> getNearbySpots(@RequestParam double mapX,
                                                       @RequestParam double mapY,
                                                       @RequestParam(defaultValue = "2000") int radius,
                                                       @RequestParam(defaultValue = "10") int numOfRows) {
        return locationSearchService.searchNearby(mapX, mapY, radius, numOfRows);
    }
}
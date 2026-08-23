package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/generate")
    public List<Spot> generateRoute(@RequestBody RouteRequest request) {
        return routeService.generateRoute(
            request.getStartLat(),
            request.getStartLon(),
            request.getSpotIds()
        );
    }

    // 요청 body 담을 내부 클래스
    public static class RouteRequest {
        private double startLat;
        private double startLon;
        private List<String> spotIds;

        public double getStartLat() { return startLat; }
        public void setStartLat(double startLat) { this.startLat = startLat; }
        public double getStartLon() { return startLon; }
        public void setStartLon(double startLon) { this.startLon = startLon; }
        public List<String> getSpotIds() { return spotIds; }
        public void setSpotIds(List<String> spotIds) { this.spotIds = spotIds; }
    }
}
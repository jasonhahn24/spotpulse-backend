package com.spotpulse.backend.service;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final SpotRepository spotRepository;

    public RouteService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    // 시작 좌표에서 출발해, 선택된 스팟들을 Nearest Neighbor 방식으로 방문 순서 정렬
    public List<Spot> generateRoute(double startLat, double startLon, List<String> spotIds) {
        List<Spot> unvisited = new ArrayList<>();
        for (String id : spotIds) {
            spotRepository.findById(id).ifPresent(unvisited::add);
        }

        List<Spot> route = new ArrayList<>();
        double currentLat = startLat;
        double currentLon = startLon;

        while (!unvisited.isEmpty()) {
            Spot nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Spot spot : unvisited) {
                double distance = DistanceCalculator.calculate(
                    currentLat, currentLon, spot.getMapY(), spot.getMapX()
                );
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = spot;
                }
            }

            route.add(nearest);
            unvisited.remove(nearest);
            currentLat = nearest.getMapY();
            currentLon = nearest.getMapX();
        }

        return route;
    }
}
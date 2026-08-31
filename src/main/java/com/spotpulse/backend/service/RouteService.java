package com.spotpulse.backend.service;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.exception.NotFoundException;
import com.spotpulse.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final SpotRepository spotRepository;
    private final WeatherService weatherService;

    public RouteService(SpotRepository spotRepository, WeatherService weatherService) {
        this.spotRepository = spotRepository;
        this.weatherService = weatherService;
    }

    // 시작 좌표에서 출발해, 선택된 스팟들을 Nearest Neighbor 방식으로 방문 순서 정렬
    // (기상 위험 스팟은 제외하고 계산)
    public List<Spot> generateRoute(double startLat, double startLon, List<String> spotIds) {
        List<Spot> unvisited = new ArrayList<>();
        for (String id : spotIds) {
            // 존재하지 않는 스팟 ID면 명확한 404 에러를 던짐 (조용히 무시하지 않음)
            Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 관광지 ID입니다: " + id));

            // 기상 위험(강수확률 60% 이상 또는 강수형태 있음) 스팟은 후보에서 제외
            if (!weatherService.isWeatherRisky(spot.getMapY(), spot.getMapX())) {
                unvisited.add(spot);
            }
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
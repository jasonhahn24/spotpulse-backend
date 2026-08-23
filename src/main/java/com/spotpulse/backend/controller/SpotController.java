package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotRepository spotRepository;

    public SpotController(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    // 테스트용 데이터 저장
    @PostMapping("/test")
    public Spot createTestSpot() {
        Spot spot = new Spot("경복궁", "고궁", 126.9770, 37.5796);
        return spotRepository.save(spot);
    }

    @PostMapping("/test-multi")
    public List<Spot> createMultipleTestSpots() {
        List<Spot> spots = new ArrayList<>();
        spots.add(spotRepository.save(new Spot("남산타워", "전망대", 126.9882, 37.5512)));
        spots.add(spotRepository.save(new Spot("홍대거리", "관광특구", 126.9236, 37.5563)));
        spots.add(spotRepository.save(new Spot("잠실롯데타워", "전망대", 127.1025, 37.5125)));
        return spots;
    }

    // 전체 조회
    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }
}
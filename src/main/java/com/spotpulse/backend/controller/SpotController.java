package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import org.springframework.web.bind.annotation.*;

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

    // 전체 조회
    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }
}
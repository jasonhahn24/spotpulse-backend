package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Contribution;
import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.ContributionRepository;
import com.spotpulse.backend.repository.SpotRepository;
import com.spotpulse.backend.service.SpotImportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotRepository spotRepository;
    private final SpotImportService spotImportService;
    private final ContributionRepository contributionRepository;

    public SpotController(SpotRepository spotRepository,
                           SpotImportService spotImportService,
                           ContributionRepository contributionRepository) {
        this.spotRepository = spotRepository;
        this.spotImportService = spotImportService;
        this.contributionRepository = contributionRepository;
    }

    // 테스트용 관광지 하나 저장
    @PostMapping("/test")
    public Spot createTestSpot() {
        Spot spot = new Spot("경복궁", "고궁", 126.9770, 37.5796);
        return spotRepository.save(spot);
    }

    // 저장된 모든 관광지 조회
    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }

    // TourAPI로부터 실제 관광지 데이터를 가져와 저장(upsert)
    @PostMapping("/import")
    public List<Spot> importSpots(@RequestParam String areaCode,
                                    @RequestParam String contentTypeId,
                                    @RequestParam(defaultValue = "10") int numOfRows) {
        return spotImportService.importSpots(areaCode, contentTypeId, numOfRows);
    }

    // 관광지 삭제
    @DeleteMapping("/{id}")
    public void deleteSpot(@PathVariable String id) {
        spotRepository.deleteById(id);
    }

    // 화제성(trendPercent) 상위 20개 관광지 조회
    @GetMapping("/trending")
    public List<Spot> getTrendingSpots() {
        return spotRepository.findTop20ByOrderByTrendPercentDesc();
    }

    // 특정 관광지에 달린 기여(팁) 목록 조회
    @GetMapping("/{id}/contributions")
    public List<Contribution> getContributionsBySpot(@PathVariable String id) {
        return contributionRepository.findBySpotId(id);
    }

    // 키워드로 특정 관광지 하나 수집 (검증용 임시 API)
    @PostMapping("/import-one")
    public Spot importOne(@RequestParam String keyword) {
        return spotImportService.importSpotByKeyword(keyword);
    }
}
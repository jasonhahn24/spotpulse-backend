package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import com.spotpulse.backend.service.RecommendationService;
import com.spotpulse.backend.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final SpotRepository spotRepository;   

    public RecommendationController(RecommendationService recommendationService, SpotRepository spotRepository) {
        this.recommendationService = recommendationService;
        this.spotRepository = spotRepository;   

    }

    // 키워드 기반 관광지 추천 (areaCd/signguCd/baseYm 직접 입력)
    @GetMapping("/test")
    public List<String> testRecommend(@RequestParam String keyword,
                                        @RequestParam String areaCd,
                                        @RequestParam String signguCd,
                                        @RequestParam String baseYm) {
        return recommendationService.getRelatedSpotNames(keyword, areaCd, signguCd, baseYm);
    }

    // Spot ID만으로 대체 스팟 추천 (areaCd/signguCd 자동 활용)
    @GetMapping("/{spotId}")
    public List<String> recommendBySpot(@PathVariable String spotId) {
        Spot spot = spotRepository.findById(spotId)
            .orElseThrow(() -> new NotFoundException("해당 관광지를 찾을 수 없습니다: " + spotId));

        String baseYm = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
        // API는 월 1회 갱신이라 항상 최신 데이터가 확실히 있는 지난달 기준으로 조회

        return recommendationService.getRelatedSpotNames(
            spot.getName(), spot.getAreaCd(), spot.getSignguCd(), baseYm
        );
    }
}
package com.spotpulse.backend.controller;

import com.spotpulse.backend.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;   

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/test")
    public List<String> testRecommend(@RequestParam String keyword,
                                        @RequestParam String areaCd,
                                        @RequestParam String signguCd,
                                        @RequestParam String baseYm) {
        return recommendationService.getRelatedSpotNames(keyword, areaCd, signguCd, baseYm);
    }

    @GetMapping("/area-codes")
    public Map getAreaCodes() {
        return recommendationService.getAreaCodes();
    }
}
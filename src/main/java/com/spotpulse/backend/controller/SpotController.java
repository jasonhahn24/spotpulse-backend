package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import com.spotpulse.backend.service.SpotImportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotRepository spotRepository;
    private final SpotImportService spotImportService;

    public SpotController(SpotRepository spotRepository, SpotImportService spotImportService) {
        this.spotRepository = spotRepository;
        this.spotImportService = spotImportService;
    }

    // 테스트용 관광지 하나 저장 
    @PostMapping("/test")
    public Spot createTestSpot() {
        Spot spot = new Spot("경복궁", "고궁", 126.9770, 37.5796);
        return spotRepository.save(spot);
    }

    // 관광지 삭제 (테스트 데이터 정리용)
    @DeleteMapping("/{id}")
    public void deleteSpot(@PathVariable String id) {
        spotRepository.deleteById(id);
    }

    // 저장된 모든 관광지 조회
    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }

    // TourAPI(지역기반 관광정보조회)로부터 실제 관광지 데이터를 가져와 Spot 컬렉션에 저장
    // areaCode: 지역코드 (예: 1=서울)
    // contentTypeId: 관광 콘텐츠 타입 (12=관광지, 14=문화시설, 15=행사/공연/축제 등)
    // numOfRows: 한 번에 가져올 개수 (기본값 10)
    @PostMapping("/import")
    public List<Spot> importSpots(@RequestParam String areaCode,
                                    @RequestParam String contentTypeId,
                                    @RequestParam(defaultValue = "10") int numOfRows) {
        return spotImportService.importSpots(areaCode, contentTypeId, numOfRows);
    }
}
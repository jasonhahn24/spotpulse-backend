package com.spotpulse.backend.controller;

import com.spotpulse.backend.repository.TrendSnapshotRepository;
import com.spotpulse.backend.service.NaverSearchService;
import com.spotpulse.backend.service.TrendCollectorScheduler;
import com.spotpulse.backend.domain.TrendSnapshot;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trends")
public class TrendController {

    private final NaverSearchService naverSearchService;
    private final TrendSnapshotRepository trendSnapshotRepository;
    private final TrendCollectorScheduler trendCollectorScheduler;

    public TrendController(NaverSearchService naverSearchService,
                            TrendSnapshotRepository trendSnapshotRepository,
                            TrendCollectorScheduler trendCollectorScheduler) {
        this.naverSearchService = naverSearchService;
        this.trendSnapshotRepository = trendSnapshotRepository;
        this.trendCollectorScheduler = trendCollectorScheduler;
    }

    @GetMapping("/test")
    public int testMentionCount(@RequestParam String keyword) {
        return naverSearchService.getTotalMentionCount(keyword);
    }

    @GetMapping("/snapshots")
    public List<TrendSnapshot> getAllSnapshots() {
        return trendSnapshotRepository.findAll();
    }

    @PostMapping("/run-now")
    public String runNow() {
        trendCollectorScheduler.runNow();
        return "배치 실행 완료";
    }
}
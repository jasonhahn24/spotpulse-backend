package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.TrendSnapshot;
import com.spotpulse.backend.repository.TrendSnapshotRepository;
import com.spotpulse.backend.service.NaverSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trends")
public class TrendController {

    private final NaverSearchService naverSearchService;
    private final TrendSnapshotRepository trendSnapshotRepository;

    public TrendController(NaverSearchService naverSearchService,
                            TrendSnapshotRepository trendSnapshotRepository) {
        this.naverSearchService = naverSearchService;
        this.trendSnapshotRepository = trendSnapshotRepository;
    }

    @GetMapping("/test")
    public int testMentionCount(@RequestParam String keyword) {
        return naverSearchService.getTotalMentionCount(keyword);
    }

    @GetMapping("/snapshots")
    public List<TrendSnapshot> getAllSnapshots() {
        return trendSnapshotRepository.findAll();
    }
}
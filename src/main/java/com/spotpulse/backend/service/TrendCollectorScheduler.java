package com.spotpulse.backend.service;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.domain.TrendSnapshot;
import com.spotpulse.backend.repository.SpotRepository;
import com.spotpulse.backend.repository.TrendSnapshotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrendCollectorScheduler {

    private final SpotRepository spotRepository;
    private final TrendSnapshotRepository trendSnapshotRepository;
    private final NaverSearchService naverSearchService;

    public TrendCollectorScheduler(SpotRepository spotRepository,
                                     TrendSnapshotRepository trendSnapshotRepository,
                                     NaverSearchService naverSearchService) {
        this.spotRepository = spotRepository;
        this.trendSnapshotRepository = trendSnapshotRepository;
        this.naverSearchService = naverSearchService;
    }

    @Scheduled(cron = "0 0 3 * * *") 
    public void collectTrends() {
        List<Spot> spots = spotRepository.findAll();

        for (Spot spot : spots) {
            int mentionCount = naverSearchService.getTotalMentionCount(spot.getName());
            TrendSnapshot snapshot = new TrendSnapshot(spot.getId(), mentionCount);
            trendSnapshotRepository.save(snapshot);
        }
    }
}
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

            // 이전 스냅샷 조회 (증가율 계산용)
            List<TrendSnapshot> history = trendSnapshotRepository.findBySpotIdOrderByCollectedAtDesc(spot.getId());

            int trendPercent = 0;
            if (!history.isEmpty()) {
                int previousCount = history.get(0).getMentionCount();
                if (previousCount > 0) {
                    trendPercent = (int) (((double) (mentionCount - previousCount) / previousCount) * 100);
                }
            }

            // 새 스냅샷 저장
            TrendSnapshot snapshot = new TrendSnapshot(spot.getId(), mentionCount);
            trendSnapshotRepository.save(snapshot);

            // Spot의 trendPercent 갱신
            spot.setTrendPercent(trendPercent);
            spotRepository.save(spot);
        }
    }

    public void runNow() {
    collectTrends();
    }
}
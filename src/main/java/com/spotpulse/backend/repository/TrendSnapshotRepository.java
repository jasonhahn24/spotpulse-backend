package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.TrendSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TrendSnapshotRepository extends MongoRepository<TrendSnapshot, String> {
    List<TrendSnapshot> findBySpotIdOrderByCollectedAtDesc(String spotId);  // 특정 스팟 이력 조회용
}
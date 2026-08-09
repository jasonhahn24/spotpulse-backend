package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.Contribution;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ContributionRepository extends MongoRepository<Contribution, String> {
    List<Contribution> findBySpotId(String spotId);  // 특정 관광지의 팁 목록 조회용
}
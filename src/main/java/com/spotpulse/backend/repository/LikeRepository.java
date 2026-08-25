package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
    boolean existsByContributionIdAndUserId(String contributionId, String userId);  // 중복 좋아요 확인용
    void deleteByContributionIdAndUserId(String contributionId, String userId);   
}
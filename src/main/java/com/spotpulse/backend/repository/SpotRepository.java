package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.Spot;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;  
import java.util.List;  

public interface SpotRepository extends MongoRepository<Spot, String> {
    Optional<Spot> findByContentId(String contentId);   // upsert 판단용
    List<Spot> findTop20ByOrderByTrendPercentDesc();   // 화제성 상위 20개, 신규 추가
    List<Spot> findByRegionName(String regionName); // 지역별 조회 추가
}
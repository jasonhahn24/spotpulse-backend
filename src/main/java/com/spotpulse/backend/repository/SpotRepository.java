package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.Spot;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;  

public interface SpotRepository extends MongoRepository<Spot, String> {
    Optional<Spot> findByContentId(String contentId);   // upsert 판단용
}
package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.Spot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpotRepository extends MongoRepository<Spot, String> {
}
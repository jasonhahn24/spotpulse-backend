package com.spotpulse.backend.repository;

import com.spotpulse.backend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
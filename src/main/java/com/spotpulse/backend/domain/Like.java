package com.spotpulse.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "likes")
@CompoundIndex(def = "{'contributionId': 1, 'userId': 1}", unique = true)
public class Like {

    @Id
    private String id;

    private String contributionId;
    private String userId;
    private LocalDateTime createdAt;

    public Like() {}

    public Like(String contributionId, String userId) {
        this.contributionId = contributionId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getContributionId() { return contributionId; }
    public String getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
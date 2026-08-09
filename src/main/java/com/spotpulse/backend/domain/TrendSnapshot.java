package com.spotpulse.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "trend_snapshots")
public class TrendSnapshot {

    @Id
    private String id;

    private String spotId;
    private int mentionCount;
    private LocalDateTime collectedAt;

    public TrendSnapshot() {}

    public TrendSnapshot(String spotId, int mentionCount) {
        this.spotId = spotId;
        this.mentionCount = mentionCount;
        this.collectedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getSpotId() { return spotId; }
    public int getMentionCount() { return mentionCount; }
    public LocalDateTime getCollectedAt() { return collectedAt; }
}
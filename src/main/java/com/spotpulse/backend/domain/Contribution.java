package com.spotpulse.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "contributions")
public class Contribution {

    @Id
    private String id;

    private String spotId;
    private String userId;
    private String content;
    private int likesCount;
    private boolean isVerified;
    private LocalDateTime createdAt;

    public Contribution() {}

    public Contribution(String spotId, String userId, String content) {
        this.spotId = spotId;
        this.userId = userId;
        this.content = content;
        this.likesCount = 0;
        this.isVerified = false;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getSpotId() { return spotId; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
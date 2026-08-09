package com.spotpulse.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "users")
public class User {

    @Id
    private String id;  // Firebase UID

    private String nickname;
    private int totalCredits;
    private String grade;
    private LocalDateTime createdAt;

    public User() {}

    public User(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.totalCredits = 0;
        this.grade = "초보 여행자";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
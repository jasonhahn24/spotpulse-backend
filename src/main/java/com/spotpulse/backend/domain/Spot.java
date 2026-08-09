package com.spotpulse.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "spots")
public class Spot {

    @Id
    private String id;

    private String name;        // 관광지명
    private String category;    // 카테고리 
    private double mapX;        // 경도
    private double mapY;        // 위도

    // 기본 생성자
    public Spot() {}

    public Spot(String name, String category, double mapX, double mapY) {
        this.name = name;
        this.category = category;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    // Getter/Setter
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getMapX() { return mapX; }
    public void setMapX(double mapX) { this.mapX = mapX; }
    public double getMapY() { return mapY; }
    public void setMapY(double mapY) { this.mapY = mapY; }
}
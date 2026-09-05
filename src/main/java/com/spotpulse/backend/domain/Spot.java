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
    private String contentId;   // TourAPI 콘텐츠 ID (upsert 판단 기준)
    private String imageUrl;    // 대표 이미지 URL 
    private int trendPercent;   // 화제성 지표(최근 언급량 등)
    private String areaCd;      // 연관관광지 API용 지역코드 (lDongRegnCd)
    private String signguCd;    // 연관관광지 API용 시군구코드 (lDongSignguCd)
    private String regionName;  // 지역명 (예: 서울, 부산 등)



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
    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getTrendPercent() { return trendPercent; }
    public void setTrendPercent(int trendPercent) { this.trendPercent = trendPercent; }
    public String getAreaCd() { return areaCd; }
    public void setAreaCd(String areaCd) { this.areaCd = areaCd; }
    public String getSignguCd() { return signguCd; }
    public void setSignguCd(String signguCd) { this.signguCd = signguCd; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
}
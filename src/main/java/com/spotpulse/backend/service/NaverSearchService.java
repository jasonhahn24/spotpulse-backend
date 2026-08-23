package com.spotpulse.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class NaverSearchService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public int getBlogMentionCount(String keyword) {
        return getMentionCount(keyword, "blog");
    }

    public int getCafeMentionCount(String keyword) {
        return getMentionCount(keyword, "cafearticle");
    }

    private int getMentionCount(String keyword, String type) {
        String url = UriComponentsBuilder
            .fromUriString("https://naverapihub.apigw.ntruss.com/search/v1/" + type)
            .queryParam("query", keyword)
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class
            );
            Object total = response.getBody().get("total");
            return total != null ? (Integer) total : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getTotalMentionCount(String spotName) {
        return getBlogMentionCount(spotName) + getCafeMentionCount(spotName);
    }
}
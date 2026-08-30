package com.spotpulse.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationSearchService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 좌표 기준 반경 내 관광지 목록 조회
     * mapX, mapY: 기준 좌표(경도, 위도)
     * radius: 반경(미터, 최대 20000)
     */
    public List<Map<String, Object>> searchNearby(double mapX, double mapY, int radius, int numOfRows) {
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorService2/locationBasedList2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=" + numOfRows
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=SPOTPULSE"
                + "&_type=json"
                + "&arrange=E"          // 거리순 정렬
                + "&mapX=" + mapX
                + "&mapY=" + mapY
                + "&radius=" + radius
                + "&contentTypeId=12";  // 관광지

            URI uri = new URI(urlStr);
            ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

            Map body = (Map) response.getBody().get("response");
            Map responseBody = (Map) body.get("body");
            Map items = (Map) responseBody.get("items");
            Object itemObj = items.get("item");

            List itemList;
            if (itemObj instanceof List) {
                itemList = (List) itemObj;
            } else {
                itemList = new ArrayList<>();
                if (itemObj instanceof Map) itemList.add(itemObj);
            }

            for (Object obj : itemList) {
                Map item = (Map) obj;
                Map<String, Object> spotInfo = new java.util.HashMap<>();
                spotInfo.put("name", item.get("title"));
                spotInfo.put("mapX", item.get("mapx"));
                spotInfo.put("mapY", item.get("mapy"));
                spotInfo.put("dist", item.get("dist"));         // 기준 좌표로부터 거리(m)
                spotInfo.put("imageUrl", item.get("firstimage"));
                results.add(spotInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
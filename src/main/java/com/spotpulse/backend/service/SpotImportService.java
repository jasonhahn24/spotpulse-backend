package com.spotpulse.backend.service;

import com.spotpulse.backend.domain.Spot;
import com.spotpulse.backend.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SpotImportService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotRepository spotRepository;

    public SpotImportService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    /**
     * 지역기반 관광정보조회로 관광지 목록을 가져와 Spot으로 저장
     * areaCode: 지역코드 (예: 1=서울)
     * contentTypeId: 콘텐츠 타입 (12=관광지, 14=문화시설, 15=행사 등)
     */
    public List<Spot> importSpots(String areaCode, String contentTypeId, int numOfRows) {
        List<Spot> savedSpots = new ArrayList<>();

        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=" + numOfRows
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=SPOTPULSE"
                + "&_type=json"
                + "&arrange=A"
                + "&areaCode=" + areaCode
                + "&contentTypeId=" + contentTypeId;

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
                String title = (String) item.get("title");
                String mapXStr = String.valueOf(item.get("mapx"));
                String mapYStr = String.valueOf(item.get("mapy"));

                if (title == null || mapXStr.equals("null") || mapYStr.equals("null")) continue;

                Spot spot = new Spot(
                    title,
                    "관광지",
                    Double.parseDouble(mapXStr),
                    Double.parseDouble(mapYStr)
                );
                savedSpots.add(spotRepository.save(spot));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedSpots;
    }
}
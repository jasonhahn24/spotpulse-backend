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
import java.util.Optional;

@Service
public class SpotImportService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotRepository spotRepository;

    public SpotImportService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public List<Spot> importSpots(String areaCode, String contentTypeId, int numOfRows) {
        List<Spot> savedSpots = new ArrayList<>();
        String regionName = mapAreaCodeToName(areaCode);

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

                String contentId = String.valueOf(item.get("contentid"));
                String title = (String) item.get("title");
                String mapXStr = String.valueOf(item.get("mapx"));
                String mapYStr = String.valueOf(item.get("mapy"));
                String firstImage = (String) item.get("firstimage");
                String areaCd = String.valueOf(item.get("lDongRegnCd"));
                String signguCdRaw = String.valueOf(item.get("lDongSignguCd"));
                String signguCd = !signguCdRaw.equals("null") ? areaCd + signguCdRaw : null;

                if (title == null || mapXStr.equals("null") || mapYStr.equals("null")) continue;

                Optional<Spot> existing = spotRepository.findByContentId(contentId);
                Spot spot = existing.orElseGet(Spot::new);

                spot.setContentId(contentId);
                spot.setName(title);
                spot.setCategory("관광지");
                spot.setMapX(Double.parseDouble(mapXStr));
                spot.setMapY(Double.parseDouble(mapYStr));
                if (firstImage != null && !firstImage.isEmpty()) {
                    spot.setImageUrl(firstImage);
                }
                if (!areaCd.equals("null")) spot.setAreaCd(areaCd);
                if (signguCd != null) spot.setSignguCd(signguCd);
                spot.setRegionName(regionName);

                savedSpots.add(spotRepository.save(spot));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedSpots;
    }

    // 키워드로 특정 관광지 하나를 콕 집어서 수집 (테스트/검증용)
    public Spot importSpotByKeyword(String keyword) {
        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=1"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=SPOTPULSE"
                + "&_type=json"
                + "&keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8")
                + "&contentTypeId=12";

            URI uri = new URI(urlStr);
            ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

            Map body = (Map) response.getBody().get("response");
            Map responseBody = (Map) body.get("body");
            Map items = (Map) responseBody.get("items");
            Object itemObj = items.get("item");

            Map item;
            if (itemObj instanceof List) {
                item = (Map) ((List) itemObj).get(0);
            } else {
                item = (Map) itemObj;
            }

            String contentId = String.valueOf(item.get("contentid"));
            String title = (String) item.get("title");
            String mapXStr = String.valueOf(item.get("mapx"));
            String mapYStr = String.valueOf(item.get("mapy"));
            String firstImage = (String) item.get("firstimage");
            String areaCd = String.valueOf(item.get("lDongRegnCd"));
            String signguCdRaw = String.valueOf(item.get("lDongSignguCd"));
            String signguCd = !signguCdRaw.equals("null") ? areaCd + signguCdRaw : null;

            Optional<Spot> existing = spotRepository.findByContentId(contentId);
            Spot spot = existing.orElseGet(Spot::new);

            spot.setContentId(contentId);
            spot.setName(title);
            spot.setCategory("관광지");
            spot.setMapX(Double.parseDouble(mapXStr));
            spot.setMapY(Double.parseDouble(mapYStr));
            if (firstImage != null && !firstImage.isEmpty()) spot.setImageUrl(firstImage);
            if (!areaCd.equals("null")) spot.setAreaCd(areaCd);
            if (signguCd != null) spot.setSignguCd(signguCd);

            return spotRepository.save(spot);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String mapAreaCodeToName(String areaCode) {
        switch (areaCode) {
            case "1": return "서울";
            case "2": return "인천";
            case "3": return "대전";
            case "4": return "대구";
            case "5": return "광주";
            case "6": return "부산";
            case "7": return "울산";
            case "8": return "세종";
            case "31": return "경기";
            case "32": return "강원";
            case "33": return "충북";
            case "34": return "충남";
            case "35": return "경북";
            case "36": return "경남";
            case "37": return "전북";
            case "38": return "전남";
            case "39": return "제주";
            default: return "기타";
        }
    }
}
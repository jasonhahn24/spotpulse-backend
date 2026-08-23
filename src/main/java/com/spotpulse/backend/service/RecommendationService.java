package com.spotpulse.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getRelatedSpotNames(String keyword, String areaCd, String signguCd, String baseYm) {
        List<String> relatedNames = new ArrayList<>();

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            String urlStr = "https://apis.data.go.kr/B551011/TarRlteTarService1/searchKeyword1"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=10"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=SPOTPULSE"
                + "&_type=json"
                + "&baseYm=" + baseYm
                + "&areaCd=" + areaCd
                + "&signguCd=" + signguCd
                + "&keyword=" + encodedKeyword;

            URI uri = new URI(urlStr);   

            ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

           Map body = (Map) response.getBody().get("response");
           Map responseBody = (Map) body.get("body");
           Object itemsObj = responseBody.get("items");

           if (!(itemsObj instanceof Map)) {
                return relatedNames;
            }

           Map items = (Map) itemsObj;
           Object itemObj = items.get("item");

           if (itemObj instanceof List) {
               for (Object item : (List) itemObj) {
                   Map itemMap = (Map) item;
                   if ("관광지".equals(itemMap.get("rlteCtgryLclsNm"))) {
                        relatedNames.add((String) itemMap.get("rlteTatsNm"));
                   }
               }
           } else if (itemObj instanceof Map) {
               Map itemMap = (Map) itemObj;
               if ("관광지".equals(itemMap.get("rlteCtgryLclsNm"))) {
                    relatedNames.add((String) itemMap.get("rlteTatsNm"));
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return relatedNames;
    }

    public Map getAreaCodes() {
        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorService2/areaCode2"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=20"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=SPOTPULSE"
                + "&_type=json";

            URI uri = new URI(urlStr);
            ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
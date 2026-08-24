package com.spotpulse.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // 특정 좌표의 위험 기상 여부 판단 (강수확률 60% 이상 또는 강수형태 있음)
    public boolean isWeatherRisky(double lat, double lon) {
        try {
            GridConverter.GridPoint grid = GridConverter.convert(lat, lon);

            String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = getNearestBaseTime();

            String urlStr = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&dataType=JSON"
                + "&base_date=" + baseDate
                + "&base_time=" + baseTime
                + "&nx=" + grid.nx
                + "&ny=" + grid.ny;

            URI uri = new URI(urlStr);
            ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

            Map body = (Map) response.getBody().get("response");
            Map responseBody = (Map) body.get("body");
            Map items = (Map) responseBody.get("items");
            List<Map> itemList = (List) items.get("item");

            for (Map item : itemList) {
                String category = (String) item.get("category");
                String value = String.valueOf(item.get("fcstValue"));

                if ("POP".equals(category) && Integer.parseInt(value) >= 60) {
                    return true;  // 강수확률 60% 이상
                }
                if ("PTY".equals(category) && !"0".equals(value)) {
                    return true;  // 강수형태 있음 (비/눈 등)
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // API 실패 시 위험 없음으로 처리 (서비스 중단 방지)
        }
    }

    // 가장 가까운 발표시각 계산 (단기예보는 3시간 단위 발표: 02:00, 05:00, 08:00 ...)
    private String getNearestBaseTime() {
        int hour = LocalTime.now().getHour();
        int[] baseHours = {2, 5, 8, 11, 14, 17, 20, 23};
        int nearest = 2;
        for (int h : baseHours) {
            if (hour >= h) nearest = h;
        }
        return String.format("%02d00", nearest);
    }
}
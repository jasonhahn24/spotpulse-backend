package com.spotpulse.backend.controller;

import com.spotpulse.backend.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/test")
    public boolean testWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.isWeatherRisky(lat, lon);
    }
}
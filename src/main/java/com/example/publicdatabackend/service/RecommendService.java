package com.example.publicdatabackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecommendService {

    private final WeatherService weatherService;
    @Value("${openai.api.key}")
    private String API_KEY;

    @Value("${openai.api.url}")
    private String URL;

    RestTemplate restTemplate = new RestTemplate();

    public RecommendService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

//    public String recommendRestaurant(String prompt) throws IOException{
//        Map<String, Object> requestBody = new HashMap<>();
//        String inputPrompt = ' ' + weatherService.getWeatherData();
//    }
}

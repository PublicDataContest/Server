//package com.example.publicdatabackend.service;
//
//import com.example.publicdatabackend.dto.map.WeatherDataDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpStatusCodeException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class RecommendService {
//
//    private final WeatherService weatherService;
//    private final RestTemplate restTemplate;
//
//    @Value("${openai.api.key}")
//    private String apiKey;
//
//    @Value("${openai.api.url}")
//    private String apiUrl;
//
//    @Autowired
//    public RecommendService(@Lazy WeatherService weatherService, RestTemplate restTemplate) {
//        this.weatherService = weatherService;
//        this.restTemplate = restTemplate;
//    }
//
//    public String recommendRestaurant(Long userId) {
//        try {
//            List<WeatherDataDto> weatherDataList = weatherService.getWeatherData();
//
//            String recommendedRestaurant = callGptApi(weatherDataList, userId);
//
//            return recommendedRestaurant;
//        } catch (Exception e) {
//            return "Unable to fetch recommendations due to an error: " + e.getMessage();
//        }
//    }
//
//    private String callGptApi(List<WeatherDataDto> weatherDataList, Long userId) {
//        String prompt = generatePrompt(weatherDataList, userId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("prompt", prompt);
//        requestBody.put("max_tokens", 150);
//        requestBody.put("model", "text-davinci-003");
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        try {
//            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
//            Map<String, Object> responseBody = responseEntity.getBody();
//            List<Map<String, String>> choices = (List<Map<String, String>>) responseBody.get("choices");
//            if (choices != null && !choices.isEmpty()) {
//                return choices.get(0).get("text").trim();
//            } else {
//                return "No recommendations available.";
//            }
//        } catch (HttpStatusCodeException e) {
//            return "Error: Unable to fetch recommendations from GPT API - " + e.getMessage();
//        }
//    }
//
//    private String generatePrompt(List<WeatherDataDto> weatherDataList, Long userId) {
//        StringBuilder promptBuilder = new StringBuilder();
//        promptBuilder.append("Given the current weather conditions: ");
//
//        for (WeatherDataDto weatherData : weatherDataList) {
//            String description = translateWeatherCategory(weatherData.getCategory(), weatherData.getFcstValue());
//            promptBuilder.append(description).append(", ");
//        }
//
//        promptBuilder.append("please suggest some suitable restaurants to visit considering the user's preferences. User ID: ").append(userId);
//        return promptBuilder.toString();
//    }
//
//    private String translateWeatherCategory(String category, String value) {
//        switch (category) {
//            case "TMP":
//                return "temperature is " + value + " degrees";
//            case "SKY":
//                return "sky condition is code " + value;
//            case "PCP":
//                return value.equals("강수없음") ? "no precipitation" : "precipitation of " + value;
//            default:
//                return category + " " + value;
//        }
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//}

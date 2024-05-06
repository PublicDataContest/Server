package com.example.publicdatabackend.service;

import com.example.publicdatabackend.dto.map.WeatherDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
@Service
public class WeatherService {
    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private String getNearestForecastTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        // 현재 시간을 3시간 단위로 반올림
        int mod = hour % 3;
        hour = (mod < 1.5) ? (hour - mod) : (hour + (3 - mod));
        // 시간을 'hh00' 형식으로 반환
        return String.format("%02d00", hour);
    }

    public List<WeatherDataDto> getWeatherData() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String baseDate = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE);
        String baseTime = "2300";
        String nearestForecastTime = getNearestForecastTime();
        int nx = 55;
        int ny = 127;

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("numOfRows", "266")
                .queryParam("pageNo", "1")
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherApiResponse> response = restTemplate.getForEntity(url, WeatherApiResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.stream(response.getBody().getResponse().getBody().getItems().getItem())
                    .filter(item -> item.getCategory().equals("TMP") || item.getCategory().equals("PCP") || item.getCategory().equals("SKY") )
                    .filter(item -> item.getFcstTime().equals(nearestForecastTime))
                    .map(item -> new WeatherDataDto(item.getCategory(), item.getFcstDate(), item.getFcstTime(), item.getFcstValue()))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Failed to fetch weather data");
        }
    }

    private static class WeatherApiResponse {
        private ApiResponse response;

        public ApiResponse getResponse() {
            return response;
        }
    }

    private static class ApiResponse {
        private ApiBody body;

        public ApiBody getBody() {
            return body;
        }
    }

    private static class ApiBody {
        private ApiItems items;

        public ApiItems getItems() {
            return items;
        }
    }

    private static class ApiItems {
        private WeatherDataDto[] item;

        public WeatherDataDto[] getItem() {
            return item;
        }
    }
}

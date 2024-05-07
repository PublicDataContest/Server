package com.example.publicdatabackend.service;

import com.example.publicdatabackend.dto.map.WeatherDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    // 서울시 중심 좌표
    private static final int DEFAULT_NX = 55;
    private static final int DEFAULT_NY = 127;

    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    /**
     * 가장 가까운 예보 기준 시간(3시간 단위) 계산
     */
    private String getNearestForecastTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int mod = hour % 3;
        hour = mod < 2 ? hour - mod : hour + (3 - mod); // 3시간 단위로 반올림
        return String.format("%02d00", hour);
    }

    /**
     * 기상청 API로부터 날씨 데이터를 가져와 필요한 항목만 반환
     */
    public List<WeatherDataDto> getWeatherData() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String baseDate = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE);
        String baseTime = "2300";
        String nearestForecastTime = getNearestForecastTime();

        // URL 구성
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1","UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("266","UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON","UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(DEFAULT_NX), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(DEFAULT_NY), "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        // 응답 읽기
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            responseBuilder.append(line);
            // API 응답 출력
            System.out.println(line);
        }
        rd.close();
        conn.disconnect();

        // JSON 응답 파싱
        String response = responseBuilder.toString();
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

        // JSONArray 내부 아이템을 JSONObject로 직접 변환
        List<WeatherDataDto> weatherList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (List.of("TMP", "PCP", "SKY").contains(item.getString("category")) && item.getString("fcstTime").equals(nearestForecastTime)) {
                weatherList.add(new WeatherDataDto(
                        item.getString("category"),
                        item.getString("fcstDate"),
                        item.getString("fcstTime"),
                        item.getString("fcstValue")));
            }
        }

        return weatherList;
    }
}

package com.example.publicdatabackend.dto.map;

public class WeatherDataDto {
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;

    public WeatherDataDto(String category, String fcstDate, String fcstTime, String fcstValue) {
        this.category = category;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
    }

    public String getCategory() {
        return category;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public String getFcstValue() {
        return fcstValue;
    }

    @Override
    public String toString() {
        return String.format("Category: %s, Forecast Date: %s, Time: %s, Value: %s",
                category, fcstDate, fcstTime, fcstValue);
    }
}
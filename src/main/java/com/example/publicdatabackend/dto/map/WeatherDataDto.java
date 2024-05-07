package com.example.publicdatabackend.dto.map;

import lombok.*;

@ToString
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataDto {
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;

    // getValue 메소드 추가
    public String getValue() {
        return fcstValue;
    }
}
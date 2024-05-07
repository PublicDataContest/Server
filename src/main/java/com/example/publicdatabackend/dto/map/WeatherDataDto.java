package com.example.publicdatabackend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class WeatherDataDto {
    private String category;//분류
    private String fcstDate;//예상날짜
    private String fcstTime;//예상시간
    private String fcstValue;//값

}

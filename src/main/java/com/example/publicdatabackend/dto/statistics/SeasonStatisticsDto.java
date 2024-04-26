package com.example.publicdatabackend.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class SeasonStatisticsDto {
    private Long spring;
    private Long summer;
    private Long fall;
    private Long winter;
}

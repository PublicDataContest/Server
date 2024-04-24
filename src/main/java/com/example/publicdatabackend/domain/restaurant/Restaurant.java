package com.example.publicdatabackend.domain.restaurant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String execLoc;
    private String addressName;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String longText;

    @Column(columnDefinition = "TEXT")
    private String photoUrl;

    private String x;
    private String y;

    private Double rating;
    private String storeId;

    private Long totalExecAmounts;
    private Long numberOfVisit;

    @Column(columnDefinition = "TEXT")
    private String currentOpeningHours;
}

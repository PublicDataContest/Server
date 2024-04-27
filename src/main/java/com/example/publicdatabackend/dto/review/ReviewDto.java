package com.example.publicdatabackend.dto.review;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ReviewDto {
    private Long id;
    private String authorName;
    private Double rating;
    private String relativeTimeDescription;
    private String photoUrl;
    private String text;
}
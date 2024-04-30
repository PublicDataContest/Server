package com.example.publicdatabackend.dto.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
@SuperBuilder
public class NormalReviewDto extends ReviewDto {
    @JsonIgnore
    private MultipartFile photoFile;

}
package com.example.publicdatabackend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;

}

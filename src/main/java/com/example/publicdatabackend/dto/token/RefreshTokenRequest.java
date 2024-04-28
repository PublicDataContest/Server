package com.example.publicdatabackend.dto.token;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;

}

package com.example.publicdatabackend.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    @NotEmpty(message = "토큰은 비어있을 수 없습니다")
    private String accessToken;

    //refreshToken을 클라이언트에 직접 반환하는 것은 권장하지 않는다는데?
    //accessToken이 필요할때마다 refreshToken을 사용해 서버에 요청할 수 있도록 수정해야함
    @NotEmpty(message = "Refresh 토큰은 비어있을 수 없습니다")
    private String refreshToken;
}

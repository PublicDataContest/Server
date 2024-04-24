package com.example.publicdatabackend.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message="아이디는 빈칸일 수 없습니다")
    private String userName;

    @NotEmpty(message = "비밀번호는 빈칸일 수 없습니다")
    private String password;
}

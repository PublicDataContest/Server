package com.example.publicdatabackend.service;

import com.example.publicdatabackend.dto.login.LoginRequest;
import com.example.publicdatabackend.dto.login.LoginResponse;
import com.example.publicdatabackend.dto.register.RegisterRequest;
import com.example.publicdatabackend.dto.register.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest body);
    LoginResponse localLogin(LoginRequest body);

    //중복된 아이디인지 확인
    boolean isUsernameTaken(String username);
    boolean isPasswordCorrect(String username, String password);



}

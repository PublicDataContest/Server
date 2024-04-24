package com.example.publicdatabackend.auth.service;

import com.example.publicdatabackend.auth.dto.LoginRequest;
import com.example.publicdatabackend.auth.dto.LoginResponse;
import com.example.publicdatabackend.auth.dto.RegisterRequest;
import com.example.publicdatabackend.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest body);
    LoginResponse localLogin(LoginRequest body);

    //중복된 아이디인지 확인
    boolean isUsernameTaken(String username);
    boolean isPasswordCorrect(String username, String password);



}

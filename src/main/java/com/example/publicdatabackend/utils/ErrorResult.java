package com.example.publicdatabackend.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {
    // USER
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "UserId Not Found Exception"),

    // SEASON
    NOT_ALLOWED_SEASON_TYPE(HttpStatus.BAD_REQUEST, "Not Allowed Season Type"),

    // SERVER
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

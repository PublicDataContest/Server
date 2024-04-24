package com.example.publicdatabackend.exception;

import com.example.publicdatabackend.utils.UsersErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UsersException extends RuntimeException {

    private final UsersErrorResult errorResult;

}

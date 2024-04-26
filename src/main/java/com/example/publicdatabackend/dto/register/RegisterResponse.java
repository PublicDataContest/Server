package com.example.publicdatabackend.dto.register;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterResponse {
    @NotEmpty
    private LocalDateTime created;
}

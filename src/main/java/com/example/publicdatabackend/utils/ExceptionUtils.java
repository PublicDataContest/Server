package com.example.publicdatabackend.utils;

import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.exception.SeasonException;
import com.example.publicdatabackend.exception.UsersException;
import com.example.publicdatabackend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionUtils {
    private final UsersRepository usersRepository;

    public Users validateUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(ErrorResult.USER_ID_NOT_FOUND));
    }

    public void validateSeason(String season) {
        if (!season.equals("spring") && !season.equals("summer") && !season.equals("fall") && !season.equals("winter"))
            throw new SeasonException(ErrorResult.NOT_ALLOWED_SEASON_TYPE);
    }
}

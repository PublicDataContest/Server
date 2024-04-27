package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Menu;
import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.map.CardDetailDto;
import com.example.publicdatabackend.dto.menu.MenuListDto;
import com.example.publicdatabackend.repository.MenuRepository;
import com.example.publicdatabackend.utils.DtoConverterUtils;
import com.example.publicdatabackend.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;

    private Users validateUser(Long userId) {
        return exceptionUtils.validateUser(userId);
    }

    public List<MenuListDto> getMenuInformation(Long userId, Long restaurantId) {
        validateUser(userId);
        List<Menu> menuList = menuRepository.findByRestaurantId(restaurantId);
        return menuList.stream()
                .map(menu -> restaurantDtoConverterUtils.buildMenuDto(menu, restaurantId))
                .collect(Collectors.toList());
    }

}

package com.example.publicdatabackend.dto.menu;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class MenuListDto {
    private String menu;
    private String price;
}

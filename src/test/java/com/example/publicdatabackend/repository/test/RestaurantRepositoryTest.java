package com.example.publicdatabackend.repository.test;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RestaurantRepositoryTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("데이터베이스_조회_테스트")
    public void 데이터베이스_조회_테스트() {
        // given
        final Restaurant restaurant = Restaurant.builder()
                .execLoc("execLoc")
                .addressName("addressName")
                .phone("phone")
                .placeName("placeName")
                .placeUrl("placeUrl")
                .longText("longText")
                .photoUrl("photoUrl")
                .x("x")
                .y("y")
                .rating(3.4)
                .storeId("storeId")
                .totalExecAmounts(20L)
                .numberOfVisit(10L)
                .currentOpeningHours("currentOpeningHours")
                .build();

        // when
        final Restaurant result = restaurantRepository.save(restaurant);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getExecLoc()).isEqualTo("execLoc");
        assertThat(result.getAddressName()).isEqualTo("addressName");
        assertThat(result.getPhone()).isEqualTo("phone");
        assertThat(result.getPlaceName()).isEqualTo("placeName");
        assertThat(result.getPlaceUrl()).isEqualTo("placeUrl");
        assertThat(result.getLongText()).isEqualTo("longText");
        assertThat(result.getPhotoUrl()).isEqualTo("photoUrl");
        assertThat(result.getX()).isEqualTo("x");
        assertThat(result.getY()).isEqualTo("y");
        assertThat(result.getRating()).isEqualTo(3.4);
        assertThat(result.getStoreId()).isEqualTo("storeId");
        assertThat(result.getTotalExecAmounts()).isEqualTo(20L);
        assertThat(result.getNumberOfVisit()).isEqualTo(10L);
        assertThat(result.getCurrentOpeningHours()).isEqualTo("currentOpeningHours");
    }
}

package com.example.publicdatabackend.controller.test;

import com.example.publicdatabackend.controller.RestaurantController;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.service.RestaurantService;
import com.example.publicdatabackend.vo.RestaurantResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {
    @InjectMocks
    private RestaurantController target;

    @Mock
    private RestaurantService restaurantService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    @DisplayName("mockMvc가NULL이아님")
    public void mockMvc가NULL이아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("매출순_데이터_출력")
    public void 매출순_데이터_출력() throws Exception {
        // given
        final String url = "/api/execAmounts";
        final Long userId = 1L;
        List<RestaurantDto.RestaurantExecAmounts> restaurantResponse = Arrays.asList(
                RestaurantDto.RestaurantExecAmounts.builder().build(),
                RestaurantDto.RestaurantExecAmounts.builder().build(),
                RestaurantDto.RestaurantExecAmounts.builder().build()
        );
        doReturn(restaurantResponse).when(restaurantService).getRestaurantExecAmountsDescDTO(userId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("userId", String.valueOf(userId))
        );

        // then
        resultActions.andExpect(status().isOk());

        final RestaurantResponse.RestaurantExecAmountsResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), RestaurantResponse.RestaurantExecAmountsResponse.class);

        assertThat(restaurantResponse.size()).isEqualTo(3);
    }
}

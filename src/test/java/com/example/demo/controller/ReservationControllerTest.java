package com.example.demo.controller;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @Test
    @DisplayName("예약 조회")
    void getAllReservations() throws Exception {
        // Given : 준비
        List<ReservationResponseDto> reservations = List.of(
                new ReservationResponseDto(1L, "user1", "item1", LocalDateTime.of(2024, 12, 16, 9, 30), LocalDateTime.of(2020, 1, 1, 1, 0))
        );
        given(reservationService.getReservations()).willReturn(reservations);

        // when : 액션
        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk()) // Then ; 결과 확인
                .andExpect(jsonPath("$[0].nickname").value("user1"))
                .andExpect(jsonPath("$[0].itemName").value("item1"));
    }
}
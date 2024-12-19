package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.service.RentalLogService;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @MockitoBean
    RentalLogService rentalLogService;

    @Test
    @DisplayName("예약 생성")
    void createReservation() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusDays(1);

        ReservationRequestDto requestDto = new ReservationRequestDto(itemId, userId, startAt, endAt);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk()); // Then
    }


    @Test
    @DisplayName("예약 조회")
    void getAllReservations() throws Exception {
        // Given
        List<ReservationResponseDto> reservations = List.of(
                new ReservationResponseDto(1L, "user1", "item1", LocalDateTime.of(2024, 12, 16, 9, 30), LocalDateTime.of(2020, 1, 1, 1, 0))
        );
        given(reservationService.getReservations()).willReturn(reservations);

        // when : 액션
        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk()) // Then 결과 확인
                .andExpect(jsonPath("$[0].nickname").value("user1"))
                .andExpect(jsonPath("$[0].itemName").value("item1"));
    }

    @Test
    @DisplayName("예약 조회 실패 테스트")
    void getAllReservationsThrowCheck() throws Exception {
        // Given
        given(reservationService.getReservations()).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "예약내용이 없습니다."));

        // when
        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isNotFound()) ;// Then 결과 확인
    }
}

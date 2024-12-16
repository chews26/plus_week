package com.example.demo.controller;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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
        ReservationStatus reservationStatus = ReservationStatus.PENDING;
        LocalDateTime startAt = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2020, 1, 1, 0, 0);
        Reservation reservation = new Reservation(1, 1, reservationStatus, startAt, endAt);
        given(reservationService.getReservations()).willReturn(moc)
    }
}
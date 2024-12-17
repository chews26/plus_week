package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalLogService rentalLogService;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    @DisplayName("예약 생성")
    void createReservation() {
        // Given
        Item testItem = new Item(1L, "Test Item 1");
        User testUser = new User(1L, "Test User 1");
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(1);

        Reservation reservation = new Reservation(testItem, testUser, ReservationStatus.PENDING, startAt, endAt);

        given(itemRepository.findById(testItem.getId())).willReturn(Optional.of(testItem));
        given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        Reservation result = reservationService.createReservation(testItem.getId(), testUser.getId(), startAt, endAt);

        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        verify(itemRepository).findById(testItem.getId());
        verify(userRepository).findById(testUser.getId());
        verify(reservationRepository).save(any(Reservation.class));

    }

    @Test
    @DisplayName("예약 조회 성공")
    void getReservations() {
        // Given
        Item testItem = new Item(1L, "Test Item 1");
        User testUser = new User(1L, "Test User 1");
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(1);

        Reservation reservation = new Reservation(testItem, testUser, ReservationStatus.PENDING, startAt, endAt);

        given(reservationRepository.findAllReservations()).willReturn(List.of(reservation));

        List<ReservationResponseDto> result = reservationService.getReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item 1", result.getFirst().getItemName());

        verify(reservationRepository).findAllReservations();
    }

    @Test
    @DisplayName("예약 검색")
    void searchAndConvertReservations() {
        Item testItem = new Item(1L, "Test Item 1");
        User testUser = new User(1L, "Test User 1");
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(1);

        Reservation reservation = new Reservation(testItem, testUser, ReservationStatus.PENDING, startAt, endAt);

        given(reservationRepository.searchReservations(testItem.getId(), testUser.getId())).willReturn(List.of(reservation));

        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(testItem.getId(), testUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test User 1", result.get(0).getNickname());
        assertEquals("Test Item 1", result.get(0).getItemName());
        assertEquals(startAt, result.get(0).getStartAt());
        assertEquals(endAt, result.get(0).getEndAt());

        verify(reservationRepository).searchReservations(testUser.getId(), testItem.getId());
    }

    @Test
    @DisplayName("예약 수정")
    void updateReservationStatus() {
        Long reservationId = 1L;
        ReservationStatus status = ReservationStatus.PENDING;
        Item testItem = new Item(1L, "Test Item 1");
        User testUser = new User(1L, "Test User 1");

        Reservation reservation = new Reservation(testItem, testUser, ReservationStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        given(reservationRepository.searchReservationIdAndStatus(reservationId)).willReturn(reservation);

        reservationService.updateReservationStatus(reservationId, status);
        assertEquals(status, reservation.getStatus());
        verify(reservationRepository).searchReservationIdAndStatus(reservationId);
    }
}
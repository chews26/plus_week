package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.RentalLog;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RentalLogRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalLogRepository rentalLogRepository;

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

        // When
        Reservation result = reservationService.createReservation(testItem.getId(), testUser.getId(), startAt, endAt);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        verify(itemRepository).findById(testItem.getId());
        verify(userRepository).findById(testUser.getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 생성 입력 null 예외")
    void createReservationNull() {
        // Given
        Long itemId = null;
        Long userId = null;
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(1);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(itemId, userId, startAt, endAt));

        // Then
        assertEquals("필수 값이 null일 수 없습니다.", exception.getMessage());
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

        // When
        List<ReservationResponseDto> result = reservationService.getReservations();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item 1", result.getFirst().getItemName());

        verify(reservationRepository).findAllReservations();
    }

    @Test
    @DisplayName("예약 조회 값 없음 예외")
    void getReservationsNotFound() {
        // Given
        given(reservationRepository.findAllReservations()).willReturn(List.of());

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> reservationService.getReservations());

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("예약내용이 없습니다.", exception.getReason());
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

        // When
        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(testItem.getId(), testUser.getId());

        // Then
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

        // When
        reservationService.updateReservationStatus(reservationId, status);

        // Then
        assertEquals(status, reservation.getStatus());
        verify(reservationRepository).searchReservationIdAndStatus(reservationId);
    }

    @Test
    @DisplayName("트랜잭션 내 예외 발생 시 롤백")
    void testTransactionRollbackOnException() {
        // Given
        Long itemId = null;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(2);

        // When
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(itemId, userId, startAt, endAt));

        // Then
        List<Reservation> reservations = reservationRepository.findAll();
        assertTrue(reservations.isEmpty());

        List<RentalLog> rentalLogs = rentalLogRepository.findAll();
        assertTrue(rentalLogs.isEmpty());
    }
}
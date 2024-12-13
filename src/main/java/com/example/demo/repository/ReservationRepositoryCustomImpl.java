package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.entity.QReservation.reservation;

@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reservation> searchReservations(Long userId, Long itemId) {
        return jpaQueryFactory
                .selectFrom(reservation)
                .leftJoin(reservation.user).fetchJoin()
                .leftJoin(reservation.item).fetchJoin()
                .where(reservation.user.id.eq(userId),
                       reservation.item.id.eq(itemId)
                )
                .fetch();
    }

    @Override
    public Reservation searchReservationIdAndStatus(Long reservationId) {
        return jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.id.eq(reservationId))
                .fetchOne();
    }
}

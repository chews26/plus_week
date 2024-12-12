package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RentalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logMessage;

    private String logType; // SUCCESS, FAILURE

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public RentalLog(Reservation reservation, String logMessage, String logType) {
        this.reservation = reservation;
        this.logMessage = logMessage;
        this.logType = logType;
    }

    public RentalLog() {}

    public void setMessage(String 테스트_로그_메세지) {
    }

    public void setActionType(String create) {
    }
}

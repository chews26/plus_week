package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING("pending"),
    APPROVED("approved"),
    EXPIRED("expired"),
    CANCELED("canceled");

    private final String name;

    ReservationStatus(String approved) {
        this.name = name();
    }
}

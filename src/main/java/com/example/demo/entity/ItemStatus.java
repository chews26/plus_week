package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum ItemStatus {
    FENDING("fending"),
    APPROVED("approved"),
    EXPIRED("expired"),
    CANCELED("canceled");

    private final String name;

    ItemStatus(String name) {
        this.name = name;
    }
}
package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum ItemStatus {
    FENDING("FENDING"),
    NORMAL("normal"),
    RESERVATION("reservation"),
    SOLDOUT("soldout");

    private final String name;

    ItemStatus(String name) {
        this.name = name;
    }
}

package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL("normal"),
    BLOCKED("blocked");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }
}

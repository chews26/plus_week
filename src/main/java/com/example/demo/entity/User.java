package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private String status; // NORMAL, BLOCKED

    @Column(nullable = false, columnDefinition = "varchar(20) default 'NORMAL'")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(String role, String email, String nickname, String password) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User() {}

    public void updateStatusToBlocked() {
        this.status = "BLOCKED";
    }

    // 연관관계

}

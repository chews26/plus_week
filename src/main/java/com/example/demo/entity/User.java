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

    @Column(nullable = false, columnDefinition = "varchar(20) default 'NORMAL'")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus; // NORMAL, BLOCKED

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(String role, String email, String nickname, String password) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userStatus = UserStatus.NORMAL;
    }

    public User() {}

    public void updateStatusToBlocked() {
        this.userStatus = UserStatus.BLOCKED;
    }

    public User(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;

    }
}

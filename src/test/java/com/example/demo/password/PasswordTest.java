package com.example.demo.password;

import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordTest {

    @Test
    public void passwordEncoderTest() {
        String password = "123456";
        String encode = PasswordEncoder.encode(password);
        assertNotEquals(password, encode);
    }

    @Test
    public void passwordMatchesTest() {
        String password = "123456";
        String encode = PasswordEncoder.encode(password);
        assertTrue(PasswordEncoder.matches(password, encode));
    }

    @Test
    public void passwordMatchesTest2() {
        String rowPassword = "123456";
        String wrongPassword = "1122233";
        String encode = PasswordEncoder.encode(rowPassword);

        assertFalse(PasswordEncoder.matches(wrongPassword, encode));
    }

}
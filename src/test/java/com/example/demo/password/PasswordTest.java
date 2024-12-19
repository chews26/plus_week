package com.example.demo.password;

import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
public class PasswordTest {


    @Test
    public void rowPasswordAndEncoderPasswordNotEqualsTest() {
        String password = "123456";
        String encode = PasswordEncoder.encode(password);
        assertNotEquals(password, encode);
    }

    @Test
    public void passwordEncoderMatchesTrueTest() {
        String password = "123456";
        String encode = PasswordEncoder.encode(password);
        assertTrue(PasswordEncoder.matches(password, encode));
    }

    @Test
    public void wrongPasswordMatchesFalseTest() {
        String rowPassword = "123456";
        String wrongPassword = "1122233";
        String encode = PasswordEncoder.encode(rowPassword);

        assertFalse(PasswordEncoder.matches(wrongPassword, encode));
    }
}

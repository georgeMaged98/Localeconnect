package com.localeconnect.app.user.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testTokenGenerationAndValidation() {

        String userName = "testuser";
        String token = jwtUtil.generateToken(userName);

        assertDoesNotThrow(() -> jwtUtil.validateToken(token));
        assertEquals(userName, jwtUtil.extractEmail(token));
    }

}

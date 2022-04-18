package com.epam.ems.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionTest {
    @Test
    void getUserPermission() {
        assertEquals("user:read",Permission.USER_READ.getUserPermission());
    }
}
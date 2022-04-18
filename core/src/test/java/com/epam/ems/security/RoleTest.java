package com.epam.ems.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void getPermissions() {
        assertTrue(Role.USER.getPermissions().contains(Permission.USER_ORDER_READ));
    }
}
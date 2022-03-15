package com.epam.ems.security;

public enum Permission {
    ORDER_READ("order:read"),
    ORDER_WRITE("order:write"),
    USER_ORDER_READ("userorder:read"),
    USER_ORDER_WRITE("userorder:write"),
    TAG_READ("tag:read"),
    TAG_WRITE("tag:write"),
    CERTIFICATE_READ("certificate:read"),
    CERTIFICATE_WRITE("certificate:write"),
    USER_READ("user:read"),
    USERINFO_READ("userinfo:read");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

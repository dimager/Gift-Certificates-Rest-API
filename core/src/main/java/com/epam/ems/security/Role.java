package com.epam.ems.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.ems.security.Permission.CERTIFICATE_READ;
import static com.epam.ems.security.Permission.CERTIFICATE_WRITE;
import static com.epam.ems.security.Permission.IMAGE_WRITE;
import static com.epam.ems.security.Permission.ORDER_READ;
import static com.epam.ems.security.Permission.ORDER_WRITE;
import static com.epam.ems.security.Permission.TAG_READ;
import static com.epam.ems.security.Permission.TAG_WRITE;
import static com.epam.ems.security.Permission.USERINFO_READ;
import static com.epam.ems.security.Permission.USER_ORDER_READ;
import static com.epam.ems.security.Permission.USER_ORDER_WRITE;
import static com.epam.ems.security.Permission.USER_READ;

public enum Role {
    USER(USER_ORDER_READ, USER_ORDER_WRITE, TAG_READ, CERTIFICATE_READ, USERINFO_READ),
    ADMIN(TAG_READ, TAG_WRITE, CERTIFICATE_READ, CERTIFICATE_WRITE, ORDER_READ, ORDER_WRITE, USER_READ, IMAGE_WRITE);

    private Set<Permission> permissions;

    Role(Permission... permissions) {
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getUserPermission()))
                .collect(Collectors.toSet());
    }
}

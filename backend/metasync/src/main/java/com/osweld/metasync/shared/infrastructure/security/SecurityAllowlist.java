package com.osweld.metasync.shared.infrastructure.security;

public final class SecurityAllowlist {

     public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/tenants/register",
            "/actuator/health",
            "/v3/api-docs/**", 
            "/swagger-ui/**"
    };

    private SecurityAllowlist() {
    }
}

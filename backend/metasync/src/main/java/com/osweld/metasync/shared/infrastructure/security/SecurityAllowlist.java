package com.osweld.metasync.shared.infrastructure.security;

import org.springframework.util.AntPathMatcher;

public final class SecurityAllowlist {

     public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/tenants/register",
            "/actuator/health",
            "/v3/api-docs/**", 
            "/swagger-ui/**"
    };

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private SecurityAllowlist() {
    }


    public static boolean isAllowlisted(String requestUri) {
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (PATH_MATCHER.match(endpoint, requestUri)) {
                return true;
            }
        }
        return false;
    }
}

package com.osweld.metasync.shared.multitenancy.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TenantFilterTest {

    private final static String TENANT_ID_HEADER = "X-Tenant-ID";
    private final static String SC_BAD_REQUEST_MSG = "Missing required header: X-Tenant-ID";
    private final static String SC_BAD_REQUEST_FORMAT_MSG = "Invalid tenant ID format";

    private TenantFilter tenantFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tenantFilter = new TenantFilter();
        AppTenantContext.clear();
    }

    @Test
    @DisplayName("Should resolve valid tenant ID to schema")
    void testValidTenantIDResolvesToSchema() throws IOException, ServletException {

        String tenantId = "tenant1";

        when(request.getRequestURI()).thenReturn("/api/v1/some-endpoint");
        when(request.getHeader(TENANT_ID_HEADER)).thenReturn(tenantId);

        String expectedSchema = SchemaName.from(new TenantAlias(tenantId)).value();

        doAnswer(invocation -> {
            assertEquals(expectedSchema, AppTenantContext.getCurrentTenant());
            return null;
        }).when(filterChain).doFilter(request, response);
        tenantFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNotNull(AppTenantContext.getCurrentTenant());
    }

    @Test
    @DisplayName("Should bypass tenant resolution for allowlisted URIs")
    void testAllowlistedURIBypassesTenantResolution() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login");

        tenantFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals(AppTenantContext.DEFAULT_TENANT_ID, AppTenantContext.getCurrentTenant());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when tenant ID header is missing")
    void testMissingTenantIDHeaderReturnsBadRequest() throws IOException, ServletException {

        when(request.getRequestURI()).thenReturn("/api/v1/some-endpoint");
        when(request.getHeader(TENANT_ID_HEADER)).thenReturn(null);

        tenantFilter.doFilter(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, SC_BAD_REQUEST_MSG);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when tenant ID header is empty")
    void testEmptyTenantIDHeaderReturnsBadRequest() throws IOException, ServletException {

        when(request.getRequestURI()).thenReturn("/api/v1/some-endpoint");
        when(request.getHeader(TENANT_ID_HEADER)).thenReturn("");

        tenantFilter.doFilter(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, SC_BAD_REQUEST_MSG);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid tenant ID")
    void testInvalidTenantIDReturnsBadRequest() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/api/v1/some-endpoint");
        when(request.getHeader(TENANT_ID_HEADER)).thenReturn("!!!invalid!!!");

        tenantFilter.doFilter(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, SC_BAD_REQUEST_FORMAT_MSG);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Should clear tenant context after request processing")
    void testContextIsClearedAfterRequest() throws IOException, ServletException {
        String tenantId = "tenant1";

        when(request.getRequestURI()).thenReturn("/api/v1/some-endpoint");
        when(request.getHeader(TENANT_ID_HEADER)).thenReturn(tenantId);

        tenantFilter.doFilter(request, response, filterChain);

        assertTrue(AppTenantContext.getCurrentTenant().equals(AppTenantContext.DEFAULT_TENANT_ID));
    }
}

package com.osweld.metasync.shared.multitenancy.filter;

import java.io.IOException;

import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.infrastructure.security.SecurityAllowlist;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String tenantId = req.getHeader(TENANT_ID_HEADER);

        try {

            if (SecurityAllowlist.isAllowlisted(req.getRequestURI())) {
                log.debug("Request URI {} is allowlisted, skipping tenant resolution", req.getRequestURI());
                AppTenantContext.setCurrentTenant(AppTenantContext.DEFAULT_TENANT_ID);
                chain.doFilter(request, response);
                return;
            }

            if (tenantId == null || tenantId.isEmpty()) {
                log.error("Missing required tenant ID header: {}", TENANT_ID_HEADER);
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing required header: " + TENANT_ID_HEADER);
                return;
            }
            try {
                TenantAlias tenantAlias = new TenantAlias(tenantId);
                SchemaName schemaName = SchemaName.from(tenantAlias);
                AppTenantContext.setCurrentTenant(schemaName.value());
                log.debug("Tenant resolved: alias={}, schema={}", tenantId, schemaName.value());
                chain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                log.error("Invalid or malformed tenant ID: {}", tenantId);
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid tenant ID format");
                return;
            }
        } finally {
            AppTenantContext.clear();
        }
    }

}

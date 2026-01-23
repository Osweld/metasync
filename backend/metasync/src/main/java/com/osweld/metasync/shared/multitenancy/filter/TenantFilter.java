package com.osweld.metasync.shared.multitenancy.filter;

import java.io.IOException;

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
            if (tenantId != null && !tenantId.isEmpty()) {
                AppTenantContext.setCurrentTenant(tenantId);
            }
            chain.doFilter(request, response);
        } finally {
            AppTenantContext.clear();
        }
    }

}

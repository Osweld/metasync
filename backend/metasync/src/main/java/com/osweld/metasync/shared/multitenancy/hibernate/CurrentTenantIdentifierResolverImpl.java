package com.osweld.metasync.shared.multitenancy.hibernate;

import java.util.Objects;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {


    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = AppTenantContext.getCurrentTenant();
        log.debug("Resolving tenant identifier: {}", tenant);
        return Objects.requireNonNullElse(tenant,AppTenantContext.DEFAULT_TENANT_ID);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }


}

package com.osweld.metasync.shared.multitenancy.context;

import java.util.Objects;

import org.slf4j.MDC;

public class AppTenantContext {

    private static final String LOGGER_TENANT_ID = "TENANT_ID";
    public static final String DEFAULT_TENANT_ID = "public";
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();


    public static String getCurrentTenant(){
        return Objects.requireNonNullElse(currentTenant.get(), DEFAULT_TENANT_ID);
    }

    public static void setCurrentTenant(String tenantId){
        MDC.put(LOGGER_TENANT_ID, tenantId);
        currentTenant.set(tenantId);
    }

    public static void clear(){
        MDC.remove(LOGGER_TENANT_ID);
        currentTenant.remove();
    }

}

package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.time.LocalDateTime;
import java.util.Objects;

public class Tenant {

    private final TenantId tenantId;
    private final TenantAlias tenantAlias;
    private final SchemaName schemaName;
    private TenantName tenantName;
    private TenantPlan plan;
    private TenantStatus status;
    private final LocalDateTime createdAt;

    private Tenant(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            TenantPlan plan,
            TenantStatus status,
            LocalDateTime createdAt) {
        this.tenantId = Objects.requireNonNull(tenantId);
        this.tenantAlias = Objects.requireNonNull(tenantAlias);
        this.schemaName = Objects.requireNonNull(schemaName);
        this.tenantName = Objects.requireNonNull(tenantName);
        this.plan = Objects.requireNonNull(plan);
        this.status = Objects.requireNonNull(status);
        this.createdAt = createdAt;
    }

    public static Tenant provision(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            TenantPlan plan,
            LocalDateTime createdAt) {
        TenantStatus initialStatus = new TenantStatus(StatusType.ACTIVE);
        return new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                plan,
                initialStatus,
                createdAt);
    }

    public static Tenant reconstitute(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            TenantPlan plan,
            TenantStatus status,
            LocalDateTime createdAt) {
        return new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                plan,
                status,
                createdAt);
    }

}

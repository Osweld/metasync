package com.osweld.metasync.tenant.internal.domain.model.tenant;

import java.time.LocalDateTime;

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
            TenantStatus status) {
        this.tenantId = tenantId;
        this.tenantAlias = tenantAlias;
        this.schemaName = schemaName;
        this.tenantName = tenantName;
        this.plan = plan;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public static Tenant provision(
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            TenantPlan plan) {
        TenantId tenantId = TenantId.generate();
        TenantStatus initialStatus = new TenantStatus(StatusType.ACTIVE);
        return new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                plan,
                initialStatus);
    }

}

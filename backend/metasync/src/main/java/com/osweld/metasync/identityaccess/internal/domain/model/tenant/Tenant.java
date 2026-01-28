package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.time.LocalDateTime;
import java.util.Objects;

import com.osweld.metasync.identityaccess.internal.domain.model.AggregateRoot;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.event.TenantProvisioned;

public class Tenant extends AggregateRoot {

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
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId must not be null");
        this.tenantAlias = Objects.requireNonNull(tenantAlias, "tenantAlias must not be null");
        this.schemaName = Objects.requireNonNull(schemaName, "schemaName must not be null");
        this.tenantName = Objects.requireNonNull(tenantName, "tenantName must not be null");
        this.plan = Objects.requireNonNull(plan, "plan must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
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

        Tenant tenant = new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                plan,
                initialStatus,
                createdAt);

        tenant.registerDomainEvent(
                TenantProvisioned.now(tenantId, tenantAlias, schemaName, tenantName, plan, initialStatus));
        return tenant;
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

    public boolean isActive() {
        return this.status.equals(new TenantStatus(StatusType.ACTIVE));
    }

    @Override
    public int hashCode() {
        int hashCodeValue =+ (151513 * 229) + tenantId.hashCode();
        return hashCodeValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tenant other = (Tenant) obj;
        return this.tenantId.equals(other.tenantId);
    }


    

}

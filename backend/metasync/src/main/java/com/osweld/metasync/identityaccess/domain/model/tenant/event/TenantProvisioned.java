package com.osweld.metasync.identityaccess.domain.model.tenant.event;

import java.time.Instant;
import java.util.Objects;

import com.osweld.metasync.identityaccess.domain.model.DomainEvent;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantStatus;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

public record TenantProvisioned(
        Instant occurredOn,
        TenantId tenantId,
        TenantAlias tenantAlias,
        SchemaName schemaName,
        TenantName tenantName,
        TenantPlan plan,
        TenantStatus status) implements DomainEvent {

    public TenantProvisioned {
        Objects.requireNonNull(occurredOn, "occurredOn must not be null");
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(tenantAlias, "tenantAlias must not be null");
        Objects.requireNonNull(schemaName, "schemaName must not be null");
        Objects.requireNonNull(tenantName, "tenantName must not be null");
        Objects.requireNonNull(plan, "plan must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }

    public static TenantProvisioned now(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            TenantPlan plan,
            TenantStatus status) {
        return new TenantProvisioned(Instant.now(), tenantId, tenantAlias, schemaName, tenantName, plan, status);
    }

}

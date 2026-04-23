package com.osweld.metasync.identityaccess.domain.model.tenant;

import java.time.Instant;
import java.util.Objects;

import com.osweld.metasync.identityaccess.domain.model.AggregateRoot;
import com.osweld.metasync.identityaccess.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.domain.model.tenant.event.TenantProvisioned;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

import lombok.Getter;

@Getter
public class Tenant extends AggregateRoot {

    private final TenantId tenantId;
    private final TenantAlias tenantAlias;
    private final SchemaName schemaName;
    private TenantName tenantName;
    private EmailAddress contactEmail;
    private TenantPlan plan;
    private TenantStatus status;
    private final Instant createdAt;


    private Tenant(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            EmailAddress contactEmail,
            TenantPlan plan,
            TenantStatus status,
            Instant createdAt) {
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId must not be null");
        this.tenantAlias = Objects.requireNonNull(tenantAlias, "tenantAlias must not be null");
        this.schemaName = Objects.requireNonNull(schemaName, "schemaName must not be null");
        this.tenantName = Objects.requireNonNull(tenantName, "tenantName must not be null");
        this.contactEmail = Objects.requireNonNull(contactEmail, "contactEmail must not be null");
        this.plan = Objects.requireNonNull(plan, "plan must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static Tenant provision(
            TenantId tenantId,
            TenantAlias tenantAlias,
            SchemaName schemaName,
            TenantName tenantName,
            EmailAddress contactEmail,
            TenantPlan plan,
            Instant createdAt) {
        TenantStatus initialStatus = new TenantStatus(StatusType.PENDING);

        Tenant tenant = new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
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
            EmailAddress contactEmail,
            TenantPlan plan,
            TenantStatus status,
            Instant createdAt) {
        return new Tenant(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                plan,
                status,
                createdAt);
    }

   



    public void activate() {
        if(this.status.value() != StatusType.PENDING) {
            throw new IllegalStateException("Only tenants in PENDING status can be activated.");
        }
        this.status = new TenantStatus(StatusType.ACTIVE);
    }


    @Override
    public int hashCode() {
         return (151513 * 229) + tenantId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tenant other = (Tenant) obj;
        return this.tenantId.equals(other.tenantId);
    } 

}

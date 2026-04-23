package com.osweld.metasync.identityaccess.infrastructure.persistence.tenant;

import java.time.Instant;
import java.util.UUID;

import com.osweld.metasync.identityaccess.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.domain.model.tenant.PlanType;
import com.osweld.metasync.identityaccess.domain.model.tenant.StatusType;
import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantStatus;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

public class TenantMother {

    public static Tenant Random() {
        return createTenant("Test Corp Tenant" + UUID.randomUUID().toString().substring(0, 5),
                "contact" + UUID.randomUUID().toString().substring(0, 5) + "@example.com");
    }

    public static Tenant createTenant(String name, String email) {
        TenantId tenantId = TenantId.generate();
        TenantName tenantName = new TenantName(name);
        TenantAlias tenantAlias = TenantAlias.derivateFrom(tenantName);
        SchemaName schemaName = SchemaName.from(tenantAlias);
        EmailAddress contactEmail = new EmailAddress(email);
        TenantPlan tenantPlan = new TenantPlan(PlanType.FREE);
        TenantStatus tenantStatus = new TenantStatus(StatusType.ACTIVE);

        return Tenant.reconstitute(tenantId, tenantAlias, schemaName, tenantName, contactEmail, tenantPlan, tenantStatus,
                Instant.now());
    }

    public static Tenant createWithAlias(String alias) {
        TenantId tenantId = TenantId.generate();
        TenantName tenantName = new TenantName("Test Corp Tenant " + alias);
        EmailAddress contactEmail = new EmailAddress("contact" + alias + "@example.com");
        TenantAlias tenantAlias = new TenantAlias(alias);
        SchemaName schemaName = SchemaName.from(tenantAlias);
        TenantPlan tenantPlan = new TenantPlan(PlanType.FREE);
        TenantStatus tenantStatus = new TenantStatus(StatusType.ACTIVE);

        return Tenant.reconstitute(tenantId, tenantAlias, schemaName, tenantName, contactEmail, tenantPlan, tenantStatus,
                Instant.now());
    }
}

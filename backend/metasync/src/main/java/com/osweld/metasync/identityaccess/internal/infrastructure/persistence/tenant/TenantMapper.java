package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import org.springframework.stereotype.Component;

import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantStatus;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

@Component
public class TenantMapper {

    public TenantJpaEntity toJpaEntity(Tenant tenant) {
        return new TenantJpaEntity(
                tenant.getTenantId().value(),
                tenant.getTenantName().value(),
                tenant.getTenantAlias().value(),
                tenant.getSchemaName().value(),
                tenant.getContactEmail().value(),
                tenant.getStatus().value(),
                tenant.getPlan().value(),
                tenant.getCreatedAt());
    }

    public Tenant toDomainModel(TenantJpaEntity entity) {
        return Tenant.reconstitute(
                new TenantId(entity.getTenantId()),
                new TenantAlias(entity.getTenantAlias()),
                new SchemaName(entity.getSchemaName()),
                new TenantName(entity.getTenantName()),
                new EmailAddress(entity.getContactEmail()),
                new TenantPlan(entity.getPlan()),
                new TenantStatus(entity.getStatus()),
                entity.getCreatedAt());
    }

}

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

     public void updateJpaEntity(TenantJpaEntity entity,Tenant tenant) {
        entity.setTenantId(tenant.getTenantId().value());
        entity.setTenantAlias(tenant.getTenantAlias().value());
        entity.setSchemaName(tenant.getSchemaName().value());
        entity.setTenantName(tenant.getTenantName().value());
        entity.setContactEmail(tenant.getContactEmail().value());
        entity.setPlan(tenant.getPlan().value());
        entity.setStatus(tenant.getStatus().value());
        entity.setCreatedAt(tenant.getCreatedAt());
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

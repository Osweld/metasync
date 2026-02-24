package com.osweld.metasync.identityaccess.internal.application.port.out;

import java.util.Optional;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;

public interface TenantRepository {

    TenantId nextIdentity();
    Tenant save(Tenant tenant);
    Optional<Tenant> findById(TenantId tenantId);
    boolean existsByTenantAlias(TenantAlias tenantAlias);
    boolean existsByContactEmail(String contactEmail);
    void deleteById(TenantId tenantId);

    
}
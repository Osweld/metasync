package com.osweld.metasync.identityaccess.internal.application.port.out;

import java.util.Optional;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantAlias;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;

public interface TenantRepository {

    TenantId nextIdentity();
    void save(Tenant tenant);
    Optional<Tenant> findById(TenantId tenantId);
    boolean existsByTenantAlias(TenantAlias tenantAlias);
    void deleteById(TenantId tenantId);

    
}
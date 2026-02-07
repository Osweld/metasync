package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.util.Optional;

public interface TenantRepository {

    TenantId nextIdentity();
    void save(Tenant tenant);
    Optional<Tenant> findById(TenantId tenantId);
    boolean existsByTenantAlias(TenantAlias tenantAlias);

    
}
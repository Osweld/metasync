package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantAlias;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HibernateTenantRepository implements TenantRepository {

    private final TenantJpaRepository tenantJpaRepository;
    private final TenantMapper tenantMapper;

    @Override
    public TenantId nextIdentity() {
       return TenantId.generate();
    }

    @Override
    public void save(Tenant tenant) {
        TenantJpaEntity entity = tenantMapper.toJpaEntity(tenant);
        tenantJpaRepository.save(entity);
    }

    @Override
    public Optional<Tenant> findById(TenantId tenantId) {
        return tenantJpaRepository.findById(tenantId.value())
                .map(tenantMapper::toDomainModel);
    }

    @Override
    public boolean existsByTenantAlias(TenantAlias tenantAlias) {
        return tenantJpaRepository.existsByTenantAlias(tenantAlias.value());
    }

    @Override
    public void deleteById(TenantId tenantId) {
        tenantJpaRepository.deleteById(tenantId.value());
    }

    

}

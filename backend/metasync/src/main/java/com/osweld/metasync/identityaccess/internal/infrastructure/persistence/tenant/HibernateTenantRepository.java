package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;

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
    public Tenant createTenant(Tenant tenant) {
        TenantJpaEntity entity = new TenantJpaEntity();
        tenantMapper.updateJpaEntity(entity, tenant);
        return tenantMapper.toDomainModel(tenantJpaRepository.save(entity));
    }

    @Override
    public Tenant updateTenant(Tenant tenant) {
        TenantJpaEntity entity = tenantJpaRepository.findById(tenant.getTenantId().value())
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenant.getTenantId().value()));
        tenantMapper.updateJpaEntity(entity, tenant);
        return tenantMapper.toDomainModel(tenantJpaRepository.save(entity));
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
    public boolean existsByContactEmail(String contactEmail) {
        return tenantJpaRepository.existsByContactEmail(contactEmail);
    }

    @Override
    public void deleteById(TenantId tenantId) {
        tenantJpaRepository.deleteById(tenantId.value());
    }


}

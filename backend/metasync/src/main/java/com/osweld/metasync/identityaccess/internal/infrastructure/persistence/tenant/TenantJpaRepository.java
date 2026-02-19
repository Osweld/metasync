package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantJpaRepository extends JpaRepository<TenantJpaEntity, UUID> {


    boolean existsByTenantAlias(String tenantAlias);

    boolean existsByContactEmail(String contactEmail);

    

}

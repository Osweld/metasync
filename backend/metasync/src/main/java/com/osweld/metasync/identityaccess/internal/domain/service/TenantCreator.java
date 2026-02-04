package com.osweld.metasync.identityaccess.internal.domain.service;

import java.time.LocalDateTime;

import com.osweld.metasync.identityaccess.internal.domain.exception.TenantNameAlreadyExistsException;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantAlias;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantCreationResult;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TenantCreator {

    private final EncryptionService encryptionService;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public TenantCreationResult prepareNewTenant(
            TenantName tenantName,
            EmailAddress ownerEmail,
            PersonName personName,
            TenantPlan tenantPlan,
            String ownerPassword) {

        if (tenantRepository.existsByName(tenantName)) {
            throw new TenantNameAlreadyExistsException("Tenant name already exists");
        }

        if (userRepository.existsByEmail(ownerEmail)) {
            throw new TenantNameAlreadyExistsException("Owner email already exists");
        }

        TenantAlias tenantAlias = TenantAlias.derivateFrom(tenantName);
        SchemaName schemaName = SchemaName.from(tenantAlias);

        TenantId tenantId = tenantRepository.nextIdentity();
        UserId ownerUserId = userRepository.nextIdentity();

        Tenant tenant = Tenant.provision(tenantId, tenantAlias, schemaName, tenantName,
                tenantPlan, LocalDateTime.now());

        User ownerUser = User.registerTenantOwner(
                ownerUserId,
                tenantId,
                personName,
                ownerPassword,
                ownerEmail,
                encryptionService
        );

        TenantCreationResult tenantCreationResult = new TenantCreationResult(tenant, ownerUser);

        return tenantCreationResult;
    }

}

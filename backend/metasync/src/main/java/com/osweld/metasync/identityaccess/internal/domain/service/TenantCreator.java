package com.osweld.metasync.identityaccess.internal.domain.service;

import java.time.LocalDateTime;

import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.exception.EmailAlreadyExistsException;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantAlias;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantCreationResult;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;

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

        if (userRepository.existsByEmail(ownerEmail)) {
            throw new EmailAlreadyExistsException("Email address already in use: " + ownerEmail.value());
        }


        LocalDateTime now = LocalDateTime.now();

        TenantAlias baseAlias = TenantAlias.derivateFrom(tenantName);
        TenantAlias finalAlias = baseAlias;

        int counter = 1;
        while (tenantRepository.existsByTenantAlias(finalAlias)) {
            finalAlias = TenantAlias.incrementCounter(baseAlias, counter);
            counter++;
        }

        SchemaName schemaName = SchemaName.from(finalAlias);

        TenantId tenantId = tenantRepository.nextIdentity();
        UserId ownerUserId = userRepository.nextIdentity();

        Tenant tenant = Tenant.provision(tenantId, finalAlias, schemaName, tenantName,
                tenantPlan, now);

        User ownerUser = User.registerTenantOwner(
                ownerUserId,
                tenantId,
                personName,
                ownerPassword,
                ownerEmail,
                now,
                encryptionService
        );

        TenantCreationResult tenantCreationResult = new TenantCreationResult(tenant, ownerUser);

        return tenantCreationResult;
    }

}

package com.osweld.metasync.identityaccess.internal.domain.service;

import com.osweld.metasync.identityaccess.internal.domain.exception.TenantNameAlreadyExistsException;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TenantProvisioningService {

    private final EncryptionService encryptionService;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public void provisionTenant(
            TenantName tenantName,
            EmailAddress adminEmail,
            PersonName personName,
            TenantPlan tenantPlan,
            String adminPassword) {

                if(tenantRepository.existsByName(tenantName)) {
                    throw new TenantNameAlreadyExistsException("Tenant name already exists");
                }

                if(userRepository.existsByEmail(adminEmail)) {
                    throw new TenantNameAlreadyExistsException("Admin email already exists");
                }
    }

}

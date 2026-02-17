package com.osweld.metasync.identityaccess.internal.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.osweld.metasync.identityaccess.internal.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.internal.application.port.out.SchemaProvisionerPort;
import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.application.usecase.ProvisionTenantUseCase;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantCreationResult;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.service.TenantCreator;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantProvisioningService implements ProvisionTenantUseCase {

    private final TenantCreator tenantCreator;
    private final UserRepository userRepository;
    private final SchemaProvisionerPort schemaProvisionerPort;
    private final TenantRepository tenantRepository;
    private final TransactionTemplate transactionTemplate;

    public void provisionTenant(ProvisionTenantCommand command) {

        TenantName tenantName = new TenantName(command.tenantName());
        EmailAddress ownerEmail = new EmailAddress(command.ownerEmail());
        PersonName personName = new PersonName(command.ownerFirstName(), command.ownerLastName());
        TenantPlan tenantPlan = TenantPlan.fromString(command.planName());

        TenantCreationResult tenantCreationResult = tenantCreator.prepareNewTenant(
                tenantName, ownerEmail, personName, tenantPlan, command.ownerPassword());

        Tenant tenant = tenantCreationResult.tenant();
        User owner = tenantCreationResult.ownerUser();

        transactionTemplate.executeWithoutResult(status -> {
            tenantRepository.save(tenant);

        });

        try {
            schemaProvisionerPort.ensureSchemaExists(tenant.getSchemaName());

            String previousSchema = AppTenantContext.getCurrentTenant();
            try {
                AppTenantContext.setCurrentTenant(tenant.getSchemaName().value());

                transactionTemplate.executeWithoutResult(status -> {
                    userRepository.save(owner);
                });

            } finally {
                AppTenantContext.setCurrentTenant(previousSchema);
            }

        } catch (Exception e) {
            handleCompensation(tenant);
            throw new RuntimeException("Failed to provision tenant: " + tenantName.value(), e);
        }
    }

    private void handleCompensation(Tenant tenant) {
        try {

            transactionTemplate.executeWithoutResult(status -> {
                tenantRepository.deleteById(tenant.getTenantId());
            });
        } catch (Exception e) {
            log.error("CRITICAL: Failed to delete tenant during compensation for tenant: " + tenant.getTenantId().value(), e);
        }

        try {
            schemaProvisionerPort.dropSchema(tenant.getSchemaName());
        } catch (Exception e) {
            log.error("CRITICAL: Failed to drop schema during compensation for tenant: " + tenant.getTenantId().value(), e);
        }
    }

}

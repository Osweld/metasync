package com.osweld.metasync.identityaccess.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.osweld.metasync.identityaccess.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.application.port.out.SchemaProvisionerPort;
import com.osweld.metasync.identityaccess.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.application.usecase.ProvisionTenantUseCase;
import com.osweld.metasync.identityaccess.domain.exception.EmailAlreadyExistsException;
import com.osweld.metasync.identityaccess.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.domain.model.user.User;
import com.osweld.metasync.identityaccess.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.domain.service.EncryptionService;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantProvisioningService implements ProvisionTenantUseCase {

    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "email %s is already in use";

    private final UserRepository userRepository;
    private final SchemaProvisionerPort schemaProvisionerPort;
    private final TenantRepository tenantRepository;
    private final EncryptionService encryptionService;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Tenant provisionTenant(ProvisionTenantCommand command) {

        TenantName tenantName = new TenantName(command.tenantName());
        EmailAddress ownerEmail = new EmailAddress(command.ownerEmail());
        PersonName personName = new PersonName(command.ownerFirstName(), command.ownerLastName());
        TenantPlan tenantPlan = TenantPlan.fromString(command.planName());

        checkEmailUniqueness(ownerEmail);
        TenantAlias tenantAlias = checkTenantAliasUniqueness(tenantName);

        Tenant tenant = generateTenant(tenantName, tenantAlias, ownerEmail, tenantPlan);

        User owner = generateOwnerUser(tenant.getTenantId(), ownerEmail, personName, command.ownerPassword());

        Tenant savedTenant = transactionTemplate.execute(status -> tenantRepository.createTenant(tenant));

        String previousSchema = AppTenantContext.getCurrentTenant();

        try {

            AppTenantContext.setCurrentTenant(tenant.getSchemaName().value());
            schemaProvisionerPort.ensureSchemaExists(tenant.getSchemaName());

            try {

                transactionTemplate.executeWithoutResult(status -> userRepository.createUser(owner));

            } catch (Exception e) {
                handleCompensation(tenant);
                throw new RuntimeException("Failed to provision tenant: " + tenantName.value(), e);
            }
        } finally {
            AppTenantContext.setCurrentTenant(previousSchema);
        }

        return savedTenant;
    }

    private void handleCompensation(Tenant tenant) {
        try {

            transactionTemplate.executeWithoutResult(status -> tenantRepository.deleteById(tenant.getTenantId()));
        } catch (Exception e) {
            log.error("CRITICAL: Failed to delete tenant during compensation for tenant: {}", tenant.getTenantId().value(), e);
        }

        try {
            schemaProvisionerPort.dropSchema(tenant.getSchemaName());
        } catch (Exception e) {
            log.error("CRITICAL: Failed to drop schema during compensation for tenant: {}", tenant.getTenantId().value(), e);
        }
    }

    private void checkEmailUniqueness(EmailAddress emailAddress) {
        if (tenantRepository.existsByContactEmail(emailAddress.value())) {
            throw new EmailAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, emailAddress.value()));
        }
    }

    private TenantAlias checkTenantAliasUniqueness(TenantName tenantName) {
        TenantAlias baseAlias = TenantAlias.derivateFrom(tenantName);
        TenantAlias finalAlias = baseAlias;

        int counter = 1;
        while (tenantRepository.existsByTenantAlias(finalAlias)) {
            finalAlias = TenantAlias.incrementCounter(baseAlias, counter);
            counter++;
        }

        return finalAlias;
    }

    private Tenant generateTenant(TenantName tenantName, TenantAlias tenantAlias, EmailAddress ownerEmail, TenantPlan tenantPlan) {
        TenantId tenantId = TenantId.generate();
        SchemaName schemaName = SchemaName.from(tenantAlias);

        LocalDateTime now = LocalDateTime.now();

        return Tenant.provision(tenantId, tenantAlias, schemaName, tenantName, ownerEmail, tenantPlan, now);
    }

    private User generateOwnerUser(TenantId tenantId, EmailAddress ownerEmail, PersonName personName, String ownerPassword) {
        UserId ownerUserId = UserId.generate();
        LocalDateTime now = LocalDateTime.now();

        return User.registerTenantOwner(
                ownerUserId,
                personName,
                ownerPassword,
                ownerEmail,
                now,
                encryptionService);
    }

}

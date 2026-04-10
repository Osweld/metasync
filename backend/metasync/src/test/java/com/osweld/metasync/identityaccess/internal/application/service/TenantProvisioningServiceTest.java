package com.osweld.metasync.identityaccess.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.PlanType;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.osweld.metasync.identityaccess.internal.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.internal.application.port.out.SchemaProvisionerPort;
import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class TenantProvisioningServiceTest {

    final ProvisionTenantCommand command = new ProvisionTenantCommand(
            "Test Tenant",
            "FREE",
            "John",
            "Doe",
            "john.doe@example.com",
            "Password@123");

    @Mock
    UserRepository userRepository;
    @Mock
    SchemaProvisionerPort schemaProvisionerPort;
    @Mock
    TenantRepository tenantRepository;
    @Mock
    TransactionTemplate transactionTemplate;
    @Mock
    EncryptionService encryptionService;

    @InjectMocks
    TenantProvisioningService tenantProvisioningService;

    @Test
    @DisplayName("Given valid tenant provisioning command, when provisionTenant is called, then it should create tenant, provision schema and save owner user")
    public void testProvisionTenant_Success() {

        when(tenantRepository.existsByContactEmail(anyString())).thenReturn(false);
        when(tenantRepository.existsByTenantAlias(any())).thenReturn(false);
        when(encryptionService.encryptPassword(anyString())).thenReturn(new EncryptedPassword("encrypted-passwordencrypted-passwordencrypted-passwordencrypted-password"));
        when(tenantRepository.save(any())).thenAnswer(invocation -> {
            return invocation.<Tenant>getArgument(0);
        });


        doAnswer(invocation -> {
            TransactionCallback<Tenant> callback = invocation.getArgument(0);
            return callback.doInTransaction(mock(TransactionStatus.class));
        }).when(transactionTemplate).execute(any());

        doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(null);
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        doAnswer(invocation -> {
            assertEquals("t_test_tenant", AppTenantContext.getCurrentTenant());
            return null;
        }).when(userRepository).save(any());

       Tenant tenant = tenantProvisioningService.provisionTenant(command);

       assertThat(tenant).isNotNull();
       assertThat(tenant.getTenantName().value()).isEqualTo(command.tenantName());
       assertThat(tenant.getSchemaName().value()).isEqualTo("t_test_tenant");
       assertThat(tenant.getPlan().value()).isEqualTo(PlanType.FREE);
       assertThat(tenant.getContactEmail().value()).isEqualTo(command.ownerEmail());
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when tenantName is blank")
    public void testProvisionTenant_BlankTenantName() {
        assertThrows(ConstraintViolationException.class, () -> {
            new ProvisionTenantCommand(
                    "",
                    "FREE",
                    "John",
                    "Doe",
                    "john.doe@example.com",
                    "Password@123");
        });
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when email is invalid")
    public void testProvisionTenant_InvalidEmail() {
        assertThrows(ConstraintViolationException.class, () -> {
            new ProvisionTenantCommand(
                    "TestTenant",
                    "FREE",
                    "John",
                    "Doe",
                    "invalid-email",
                    "Password@123");
        });
    }

}

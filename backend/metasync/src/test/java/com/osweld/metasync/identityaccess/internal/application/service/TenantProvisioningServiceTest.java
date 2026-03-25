package com.osweld.metasync.identityaccess.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

import java.util.function.Consumer;

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

        ProvisionTenantCommand command = new ProvisionTenantCommand(
                "Test Tenant",
                "FREE",
                "John",
                "Doe",
                "john.doe@example.com",
                "Password@123");

        given(encryptionService.encryptPassword(command.ownerPassword())).willReturn(
                new EncryptedPassword("encryptedPasswordencryptedPasswordencryptedPasswordencryptedPassword"));

        

        doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(null);
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        doAnswer(invocation -> {
            TransactionCallback<Void> callback = invocation.getArgument(0);
            callback.doInTransaction(mock(TransactionStatus.class));
            return null;
        }).when(transactionTemplate).execute(any());
        
        doAnswer(invocation -> {
            assertEquals("t_test_tenant", AppTenantContext.getCurrentTenant());
            return null;
        }).when(userRepository).save(any());

       tenantProvisioningService.provisionTenant(command);
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

package com.osweld.metasync.identityaccess.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.apache.tomcat.util.http.parser.TE;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.osweld.metasync.identityaccess.internal.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.internal.application.port.out.SchemaProvisionerPort;
import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantCreationResult;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.service.TenantCreator;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class TenantProvisioningServiceTest {

    @Mock
    TenantCreator tenantCreator;
    @Mock
    UserRepository userRepository;
    @Mock
    SchemaProvisionerPort schemaProvisionerPort;
    @Mock
    TenantRepository tenantRepository;
    @Mock
    TransactionTemplate transactionTemplate;

    @InjectMocks
    TenantProvisioningService tenantProvisioningService;

    @Test
    @DisplayName("Given valid tenant provisioning command, when provisionTenant is called, then it should create tenant, provision schema and save owner user")
    public void testProvisionTenant_Success() {

        ProvisionTenantCommand command = new ProvisionTenantCommand(
                "TestTenant",
                "FREE",
                "John",
                "Doe",
                "john.doe@example.com",
                "Password@123");

        Tenant dummyTenant = mock(Tenant.class);
        User dummyOwner = mock(User.class);
        SchemaName dummySchema = new SchemaName("t_test_tenant");

        when(dummyTenant.getSchemaName()).thenReturn(dummySchema);

        when(tenantCreator.prepareNewTenant(
                any(), any(), any(), any(), any())).thenReturn(new TenantCreationResult(dummyTenant, dummyOwner));

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

        InOrder inOrder = Mockito.inOrder(tenantRepository, schemaProvisionerPort, userRepository);
        inOrder.verify(tenantRepository).save(dummyTenant);
        inOrder.verify(schemaProvisionerPort).ensureSchemaExists(dummySchema);
        inOrder.verify(userRepository).save(dummyOwner);
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

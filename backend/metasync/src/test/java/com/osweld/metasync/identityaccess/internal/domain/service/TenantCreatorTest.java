package com.osweld.metasync.identityaccess.internal.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.exception.EmailAlreadyExistsException;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.PlanType;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantCreationResult;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

@ExtendWith(MockitoExtension.class)
public class TenantCreatorTest {

    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "email %s is already in use";

    private final TenantName tenantName = new TenantName("Acme Corp");
    private final EmailAddress email = new EmailAddress("admin@acme.com");
    private final PersonName personName = new PersonName("John", "Doe");
    private final TenantPlan plan = new TenantPlan(PlanType.FREE);
    private final String password = "password123";

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TenantCreator tenantCreator;

    @Test
    @DisplayName("Should prepare new tenant successfully when alias is available")
    void shouldPrepareNewTenantSuccessfullyWhenAliasIsAvailable() {
        given(tenantRepository.existsByContactEmail(email.value())).willReturn(false);
        given(tenantRepository.existsByTenantAlias(any())).willReturn(false);

        TenantId tenantId = new TenantId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());

        given(tenantRepository.nextIdentity()).willReturn(tenantId);
        given(userRepository.nextIdentity()).willReturn(userId);

        given(encryptionService.encryptPassword(password)).willReturn(
                new EncryptedPassword("encryptedPasswordencryptedPasswordencryptedPasswordencryptedPassword"));

        TenantCreationResult result = tenantCreator.prepareNewTenant(tenantName, email, personName, plan, password);

        assertThat(result.tenant()).isNotNull();
        assertThat(result.tenant().getTenantId()).isEqualTo(tenantId);
        assertThat(result.tenant().getTenantAlias()).isEqualTo(TenantAlias.derivateFrom(tenantName));
        assertThat(result.tenant().getTenantName()).isEqualTo(tenantName);
        assertThat(result.tenant().getContactEmail()).isEqualTo(email);
        assertThat(result.tenant().getPlan()).isEqualTo(plan);

        assertThat(result.ownerUser()).isNotNull();
        assertThat(result.ownerUser().getUserId()).isEqualTo(userId);
        assertThat(result.ownerUser().getTenantId()).isEqualTo(tenantId);
        assertThat(result.ownerUser().getUserName()).isEqualTo(personName);
        assertThat(result.ownerUser().getEmailAddress()).isEqualTo(email);

        verify(tenantRepository).existsByContactEmail(email.value());
    }

    @Test
    @DisplayName("Should increment alias counter if alias already exists")
    void shouldIncrementAliasIfExists() {

        given(tenantRepository.existsByContactEmail(any())).willReturn(false);

        TenantAlias baseAlias = TenantAlias.derivateFrom(tenantName);
        TenantAlias incrementedAlias = TenantAlias.incrementCounter(baseAlias, 1);

        given(tenantRepository.existsByTenantAlias(baseAlias)).willReturn(true);
        given(tenantRepository.existsByTenantAlias(incrementedAlias)).willReturn(false);

        TenantId tenantId = new TenantId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());

        given(tenantRepository.nextIdentity()).willReturn(tenantId);
        given(userRepository.nextIdentity()).willReturn(userId);

        given(encryptionService.encryptPassword(password)).willReturn(
                new EncryptedPassword("encryptedPasswordencryptedPasswordencryptedPasswordencryptedPassword"));

        TenantCreationResult result = tenantCreator.prepareNewTenant(tenantName, email, personName, plan, password);

        assertThat(result.tenant().getTenantAlias()).isEqualTo(incrementedAlias);

        verify(tenantRepository).existsByTenantAlias(baseAlias);
        verify(tenantRepository).existsByTenantAlias(incrementedAlias);
    }

    @Test
    @DisplayName("Should throw exception if email is already in use")
    void shouldThrowExceptionIfEmailAlreadyInUse() {

        given(tenantRepository.existsByContactEmail(email.value())).willReturn(true);
       
        assertThatThrownBy(() -> tenantCreator.prepareNewTenant(tenantName, email, personName, plan, password))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, email.value()));
    }

}
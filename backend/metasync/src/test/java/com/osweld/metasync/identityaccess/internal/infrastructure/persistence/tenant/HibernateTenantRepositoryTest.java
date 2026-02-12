package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import com.osweld.metasync.shared.infrastructure.AbstractIntegrationTest;

import jakarta.persistence.EntityManager;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantAlias;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ HibernateTenantRepository.class, TenantMapper.class })
public class HibernateTenantRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private HibernateTenantRepository tenantRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should save a tenant and retrieve it by ID")
    void shouldSaveAndFindById() {
        Tenant tenant = TenantMother.Random();

        tenantRepository.save(tenant);

        Optional<Tenant> found = tenantRepository.findById(tenant.getTenantId());

        assertThat(found).isPresent();
        assertThat(found.get())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(tenant);
    }

    @Test
    @DisplayName("Should return true if tenant alias exists")
    void shouldReturnTrueIfTenantAliasExists() {
        Tenant tenant = TenantMother.Random();
        tenantRepository.save(tenant);
        boolean exists = tenantRepository.existsByTenantAlias(tenant.getTenantAlias());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false if tenant alias does not exist")
    void shouldReturnFalseIfTenantAliasDoesNotExist() {
        boolean exists = tenantRepository
                .existsByTenantAlias(TenantAlias.derivateFrom(new TenantName("NonExistentTenant")));
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should thow an exception when trying to save a tenant with a duplicate alias")
    void shouldThrowExceptionWhenSavingTenantWithDuplicateAlias() {
        Tenant tenant1 = TenantMother.createWithAlias("duplicate-alias");
        tenantRepository.save(tenant1);
        entityManager.flush();
        entityManager.clear();

        Tenant tenant2 = TenantMother.createWithAlias("duplicate-alias");

        assertThatThrownBy(() -> {
            tenantRepository.save(tenant2);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class);
    }
}

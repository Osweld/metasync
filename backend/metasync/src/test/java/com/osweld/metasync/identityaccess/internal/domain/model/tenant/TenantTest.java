package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.identityaccess.internal.domain.model.DomainEvent;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.event.TenantProvisioned;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TenantTest {

    private final TenantId tenantId = new TenantId(UUID.randomUUID());
    private final TenantAlias tenantAlias = new TenantAlias("acme-corp");
    private final SchemaName schemaName = new SchemaName("t_acme_corp");
    private final TenantName tenantName = new TenantName("Acme Corp");
    private final EmailAddress contactEmail = new EmailAddress("contact@acme-corp.com");
    private final TenantPlan tenantPlan = new TenantPlan(PlanType.FREE);
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    @DisplayName("Should provision a new Tenant with initial ACTIVE status")
    void testProvisionTenant() {
        Tenant tenant = Tenant.provision(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                now);

        assertThat(tenant).isNotNull();
        assertThat(tenant.isActive()).isTrue();

        List<DomainEvent> events = tenant.pullDomainEvents();

        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(TenantProvisioned.class);

        TenantProvisioned event = (TenantProvisioned) events.get(0);

        assertThat(event.tenantId()).isEqualTo(tenantId);
        assertThat(event.tenantAlias()).isEqualTo(tenantAlias);
        assertThat(event.schemaName()).isEqualTo(schemaName);
        assertThat(event.tenantName()).isEqualTo(tenantName);
        assertThat(event.plan()).isEqualTo(tenantPlan);

        assertThat(event.occurredOn()).isNotNull();
    }

    @Test
    @DisplayName("Should reconstitute a Tenant with given status")
    void testReconstituteTenant() {
        TenantStatus status = new TenantStatus(StatusType.INACTIVE);
        Tenant tenant = Tenant.reconstitute(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                status,
                now);
        assertThat(tenant).isNotNull();
        assertThat(tenant.isActive()).isFalse();

        assertThat(tenant.pullDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("Should not provision Tenant with null identifier")
    void testProvisionTenantWithNullIdentifier() {
        assertThatThrownBy(() -> Tenant.provision(
                null,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                now))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("tenantId must not be null");
    }

    @Test
    @DisplayName("Entities with same ID should be considered equal")
    void testTenantEqualityById() {
        Tenant tenant1 = Tenant.reconstitute(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                new TenantStatus(StatusType.ACTIVE),
                now);

        Tenant tenant2 = Tenant.reconstitute(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                new TenantStatus(StatusType.ACTIVE),
                now);

        assertThat(tenant1).isEqualTo(tenant2);
        assertThat(tenant1.hashCode()).isEqualTo(tenant2.hashCode());
    }

    @Test
    @DisplayName("Entities with different IDs should not be equal")
    void testEntityInequality(){
        TenantId differentId = new TenantId(UUID.randomUUID());

         Tenant tenant1 = Tenant.reconstitute(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                new TenantStatus(StatusType.ACTIVE),
                now);

        Tenant tenant2 = Tenant.reconstitute(
                differentId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                new TenantStatus(StatusType.ACTIVE),
                now);

        assertThat(tenant1).isNotEqualTo(tenant2);
    }


}
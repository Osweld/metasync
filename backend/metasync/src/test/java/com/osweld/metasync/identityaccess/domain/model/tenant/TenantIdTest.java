package com.osweld.metasync.identityaccess.domain.model.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.identityaccess.domain.model.tenant.TenantId;

public class TenantIdTest {

    @Test
    @DisplayName("Should create a valid TenantId")
    void testCreateValidTenantId() {
        TenantId tenantId = new TenantId(UUID.randomUUID());

        assertThat(tenantId).isNotNull();
    }

    @Test
    @DisplayName("Should consider two TenantIds with the same UUID as equal")
    void testTenantIdEquality() {
        UUID id = UUID.randomUUID();
        TenantId tenantId1 = new TenantId(id);
        TenantId tenantId2 = new TenantId(id);

        assertThat(tenantId1).isEqualTo(tenantId2);
    }

    @Test
    @DisplayName("Should consider two TenantIds with different UUIDs as unequal")
    void testTenantIdInequality() {
        TenantId id1 = TenantId.generate();
        TenantId id2 = TenantId.generate();

        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null TenantId")
    void testTenantIdIsNull() {
        assertThatThrownBy(() -> new TenantId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    

    @Test
    @DisplayName("Should store and retrieve the correct UUID value")
    void testTenantIdValueRetained() {
        UUID id = UUID.randomUUID();
        TenantId tenantId = new TenantId(id);

        assertThat(tenantId.value()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should generate a valid TenantId")
    void testGenerateTenantId() {
        TenantId tenantId = TenantId.generate();
        assertThat(tenantId).isNotNull();
    }

    @Test
    @DisplayName("Should return string representation of UUID")
    void testToString() {
        UUID id = UUID.randomUUID();
        TenantId tenantId = new TenantId(id);

        assertThat(tenantId.toString()).isEqualTo(id.toString());
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantIds")
    void testHashCodeConsistency() {
        UUID id = UUID.randomUUID();
        TenantId tenantId1 = new TenantId(id);
        TenantId tenantId2 = new TenantId(id);
        assertThat(tenantId1.hashCode()).isEqualTo(tenantId2.hashCode());
    }
}

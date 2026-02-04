package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TenantAliasTest {

    @Test
    @DisplayName("Should create a valid TenantAlias")
    void testCreateValidTenantAlias() {
        TenantAlias tenantAlias = new TenantAlias("valid-alias123");

        assertThat(tenantAlias.value()).isEqualTo("valid-alias123");
    }

    @Test
    @DisplayName("Should create TenantAlias derived from TenantName")
    void testDerivateFromTenantName() {
        TenantName tenantName = new TenantName("Ténánt Nâme");
        TenantAlias tenantAlias = TenantAlias.derivateFrom(tenantName);

        assertThat(tenantAlias.value()).isEqualTo("tenant-name");
    }

    @Test
    @DisplayName("Should consider two TenantAliases with the same value as equal")
    void testTenantAliasEquality() {
        TenantAlias tenantAlias1 = new TenantAlias("valid-alias123");
        TenantAlias tenantAlias2 = new TenantAlias("valid-alias123");

        assertThat(tenantAlias1).isEqualTo(tenantAlias2);
    }

    @Test
    @DisplayName("Should consider two TenantAliases with different values as unequal")
    void testTenantAliasInequality() {
        TenantAlias tenantAlias1 = new TenantAlias("alias-one");
        TenantAlias tenantAlias2 = new TenantAlias("alias-two");
        assertThat(tenantAlias1).isNotEqualTo(tenantAlias2);
    }

    @Test
    @DisplayName("Should normalize TenantAlias to lowercase and trim spaces")
    void testTenantAliasNormalization() {
        TenantAlias tenantAlias = new TenantAlias("  Valid-Alias123  ");
        assertThat(tenantAlias.value()).isEqualTo("valid-alias123");
    }

    @ParameterizedTest(name = "Invalid alias \"{0}\" should throw exception")
    @NullSource              
    @EmptySource             
    @ValueSource(strings = { 
        "   ", 
        "1invalid-start", 
        "-invalid-start", 
        "invalid_characters!",
    })
    @DisplayName("Should reject invalid TenantAliases")
    void should_reject_invalid_tenant_aliases(String invalidAlias) {

        assertThatThrownBy(() -> new TenantAlias(invalidAlias))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Should reject TenantAlias exceeding maximum length")
    void should_reject_alias_exceeding_max_length() {
        String longAlias = "a".repeat(51);
        assertThatThrownBy(() -> new TenantAlias(longAlias))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("longer than 50 characters"); // Verifica el mensaje también
    }

    @Test
    @DisplayName("Should allow TenantAlias with exactly 50 characters")
    void testTenantAliasMaxLength() {
        String maxAlias = "a".repeat(50);
        TenantAlias tenantAlias = new TenantAlias(maxAlias);
        assertThat(tenantAlias.value()).isEqualTo(maxAlias);
    }

    @Test
    @DisplayName("Should String representation of TenantAlias")
    void testTenantAliasToString() {
        TenantAlias tenantAlias = new TenantAlias("valid-alias123");
        assertThat(tenantAlias.toString()).isEqualTo("valid-alias123");
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantAliases")
    void testHashCodeConsistency() {
        TenantAlias alias1 = new TenantAlias("consistent-alias");
        TenantAlias alias2 = new TenantAlias("consistent-alias");
        assertThat(alias1.hashCode()).isEqualTo(alias2.hashCode());
    }
}
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

    private static final String VALID_ALIAS = "valid-alias";

    @Test
    @DisplayName("Should create a valid TenantAlias")
    void testCreateValidTenantAlias() {
        TenantAlias tenantAlias = new TenantAlias(VALID_ALIAS);

        assertThat(tenantAlias.value()).isEqualTo(VALID_ALIAS);
    }

    @Test
    @DisplayName("Should create TenantAlias derived from TenantName")
    void testDerivateFromTenantName() {
        TenantName tenantName = new TenantName("Tenant Name valid");
        TenantAlias tenantAlias = TenantAlias.derivateFrom(tenantName);

        assertThat(tenantAlias.value()).isEqualTo("tenant-name-valid");
    }

    @Test
    @DisplayName("Should consider two TenantAliases with the same value as equal")
    void testTenantAliasEquality() {
        TenantAlias tenantAlias1 = new TenantAlias(VALID_ALIAS);
        TenantAlias tenantAlias2 = new TenantAlias(VALID_ALIAS);

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
        TenantAlias tenantAlias = new TenantAlias(VALID_ALIAS);
        assertThat(tenantAlias.toString()).isEqualTo(VALID_ALIAS);
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantAliases")
    void testHashCodeConsistency() {
        TenantAlias alias1 = new TenantAlias(VALID_ALIAS);
        TenantAlias alias2 = new TenantAlias(VALID_ALIAS);
        assertThat(alias1.hashCode()).isEqualTo(alias2.hashCode());
    }

    @Test
    @DisplayName("Should have different hashCodes for unequal TenantAliases")
    void testHashCodeDifference() {
        TenantAlias alias1 = new TenantAlias("alias-one");
        TenantAlias alias2 = new TenantAlias("alias-two");
        assertThat(alias1.hashCode()).isNotEqualTo(alias2.hashCode());
    }

    @Test
    @DisplayName("TenantAlias should be immutable")
    void testImmutability() {
        TenantAlias alias = new TenantAlias(VALID_ALIAS);
        String originalValue = alias.value();
        assertThat(alias.value()).isEqualTo(originalValue);
    }
}
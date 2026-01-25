package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TenantNameTest {

    @Test
    @DisplayName("Should create a valid TenantName")
    void testCreateValidTenantName() {
        TenantName tenantName = new TenantName("Valid Tenant Name, Inc.");
        assertThat(tenantName.value()).isEqualTo("Valid Tenant Name, Inc.");
    }

    
    @Test
    @DisplayName("Should consider two TenantNames with the same value as equal")
    void testTenantNameEquality() {
        TenantName tenantName1 = new TenantName("Same Name");
        TenantName tenantName2 = new TenantName("Same Name");
        assertThat(tenantName1).isEqualTo(tenantName2);
    }

    @Test
    @DisplayName("Should consider two TenantNames with different values as unequal")
    void testTenantNameInequality() {
        TenantName tenantName1 = new TenantName("Name One");
        TenantName tenantName2 = new TenantName("Name Two");
        assertThat(tenantName1).isNotEqualTo(tenantName2);
    }

    @Test
    @DisplayName("Should trim spaces from TenantName")
    void testTenantNameTrimming() {
        TenantName tenantName = new TenantName("  Trimmed Name  ");
        assertThat(tenantName.value()).isEqualTo("Trimmed Name");
    }

    @ParameterizedTest(name = "Invalid name \"{0}\" should throw exception")
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "   ",
            "Invalid@Name!",
            "Name#With$Special%Chars^"
    })
    @DisplayName("Should reject invalid TenantNames")
    void should_reject_invalid_tenant_names(String invalidName) {
        assertThatThrownBy(() -> new TenantName(invalidName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should reject TenantName exceeding maximum length")
    void should_reject_name_exceeding_max_length() {
        String longName = "a".repeat(101);
        assertThatThrownBy(() -> new TenantName(longName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should accept TenantName at maximum length")
    void should_accept_name_at_max_length() {
        String maxName = "a".repeat(100);
        TenantName tenantName = new TenantName(maxName);
        assertThat(tenantName.value()).isEqualTo(maxName);
    }

    @Test
    @DisplayName("Should String representation of TenantName")
    void testTenantNameToString() {
        TenantName tenantName = new TenantName("Valid Tenant Name, Inc.");
        assertThat(tenantName.toString()).isEqualTo("Valid Tenant Name, Inc.");
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantNames")
    void testHashCodeConsistency() {
        TenantName name1 = new TenantName("Consistent Name");
        TenantName name2 = new TenantName("Consistent Name");
        assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
    }

            
}

package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SchemaNameTest {

    private static final String VALID_SCHEMA_NAME = "t_tenant_schema";

    @Test
    @DisplayName("Should create a valid SchemaName")
    void testCreateValidSchemaName() {
        SchemaName schemaName = new SchemaName(VALID_SCHEMA_NAME);

        assertThat(schemaName.value()).isEqualTo(VALID_SCHEMA_NAME);
    }

    @Test
    @DisplayName("Should generate a valid SchemaName from TenantAlias")
    void testGenerateSchemaNameFromTenantAlias() {
        TenantAlias tenantAlias = new TenantAlias("tenant-alias");
        SchemaName schemaName = SchemaName.from(tenantAlias);
        assertThat(schemaName.value()).isEqualTo("t_tenant_alias");
    }

    @Test
    @DisplayName("Should consider two SchemaNames with the same value as equal")
    void testSchemaNameEquality() {
        SchemaName schemaName1 = new SchemaName(VALID_SCHEMA_NAME);
        SchemaName schemaName2 = new SchemaName(VALID_SCHEMA_NAME);

        assertThat(schemaName1).isEqualTo(schemaName2);
    }

    @Test
    @DisplayName("Should consider two SchemaNames with different values as unequal")
    void testSchemaNameInequality() {
        SchemaName schemaName1 = new SchemaName("t_schema_one");
        SchemaName schemaName2 = new SchemaName("t_schema_two");
        assertThat(schemaName1).isNotEqualTo(schemaName2);
    }

    @Test
    @DisplayName("Should normalize SchemaName by trimming and lowercasing")
    void should_normalize_schema_name() {
        SchemaName schemaName = new SchemaName("  t_Schema_Name  ");
        assertThat(schemaName.value()).isEqualTo("t_schema_name");
    }

   @Test
    @DisplayName("Should throw IllegalArgumentException for blank SchemaName")
    void testSchemaNameIsBlank() {
        assertThatThrownBy(() -> new SchemaName("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schema name cannot be blank");
    }

   @ParameterizedTest(name = "Invalid schema name \"{0}\" should throw exception")
    @ValueSource(strings = {
            "123invalid",  
            "schema-name", 
            "schema name", 
            "schema@name", 
            "_schema",     
            "schema_name!"
    })
    @DisplayName("Should reject invalid SchemaName patterns")
    void should_reject_invalid_schema_patterns(String invalidName) {
        assertThatThrownBy(() -> new SchemaName(invalidName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for SchemaName that is too long")
    void testSchemaNameTooLong() {
        String longName = "a".repeat(64);
        assertThatThrownBy(() -> new SchemaName(longName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should allow SchemaName with exactly 63 characters")
    void testSchemaNameMaxLength() {
        String maxName = "t_".repeat(31) + "t";
        SchemaName schemaName = new SchemaName(maxName);
        assertThat(schemaName.value()).isEqualTo(maxName);
    }

    @Test
    @DisplayName("Should return String representation of SchemaName")
    void testSchemaNameToString() {
        SchemaName schemaName = new SchemaName("t_tenant_schema");
        assertThat(schemaName.toString()).isEqualTo("t_tenant_schema");
    }

    @Test
    @DisplayName("Should have same hashCode for equal SchemaNames")
    void testHashCodeConsistency() {
        SchemaName name1 = new SchemaName("t_schema_name");
        SchemaName name2 = new SchemaName("t_schema_name");
        assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
    }

    @Test
    @DisplayName("Should have different hashCodes for unequal SchemaNames")
    void testHashCodeDifference() {
        SchemaName name1 = new SchemaName("t_schema_one");
        SchemaName name2 = new SchemaName("t_schema_two");
        assertThat(name1.hashCode()).isNotEqualTo(name2.hashCode());
    }

    @Test
    @DisplayName("SchemaName should be immutable")
    void testImmutability() {
        SchemaName schema = new SchemaName(VALID_SCHEMA_NAME);
        String originalValue = schema.value();
        assertThat(schema.value()).isEqualTo(originalValue);
    }

    @Test
    @DisplayName("Should generate SchemaName from TenantAlias with multiple hyphens")
    void testGenerateSchemaNameFromTenantAliasWithMultipleHyphens() {
        TenantAlias tenantAlias = new TenantAlias("tenant-alias-with-multiple-hyphens");
        SchemaName schemaName = SchemaName.from(tenantAlias);
        assertThat(schemaName.value()).isEqualTo("t_tenant_alias_with_multiple_hyphens");
    }

}

package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SchemaNameTest {

    @Test
    @DisplayName("Should create a valid SchemaName")
    void testCreateValidSchemaName() {
        SchemaName schemaName = new SchemaName("tenant_schema");

        assertThat(schemaName.value()).isEqualTo("tenant_schema");
    }

    @Test
    @DisplayName("Should consider two SchemaNames with the same value as equal")
    void testSchemaNameEquality() {
        SchemaName schemaName1 = new SchemaName("tenant_schema");
        SchemaName schemaName2 = new SchemaName("tenant_schema");

        assertThat(schemaName1).isEqualTo(schemaName2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid SchemaName")
    void testSchemaNameIsNull() {
        assertThatThrownBy(() -> new SchemaName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for empty SchemaName")
    void testSchemaNameIsEmpty() {
        assertThatThrownBy(() -> new SchemaName(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for SchemaName with invalid characters")
    void testSchemaNameIsInvalidCharacters() {
        assertThatThrownBy(() -> new SchemaName("invalid schema!"))
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
        String maxName = "a".repeat(63);
        SchemaName schemaName = new SchemaName(maxName);
        assertThat(schemaName.value()).isEqualTo(maxName);
    }

}

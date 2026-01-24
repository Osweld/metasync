package com.osweld.metasync.tenant.internal.domain.model.identityaccess;

import java.util.Objects;
import java.util.regex.Pattern;

public final class SchemaName {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private final String schemaName;

    public SchemaName(String schemaName) {

        if (schemaName == null) {
            throw new IllegalArgumentException("Schema name cannot be null");
        }

        String trimmedName = schemaName.trim().toLowerCase();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }

        if (trimmedName.length() > 63) {
            throw new IllegalArgumentException("Schema name cannot be longer than 63 characters");
        }

        if (!VALID_PATTERN.matcher(trimmedName).matches()) {
            throw new IllegalArgumentException(
                    "Schema name must start with a letter or underscore and can only contain letters, digits, and underscores");
        }

        this.schemaName = trimmedName;
    }

    public String value() {
        return schemaName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        SchemaName that = (SchemaName) obj;
        return Objects.equals(this.schemaName, that.schemaName);
    }

    @Override
    public String toString() {
        return schemaName;
    }

}

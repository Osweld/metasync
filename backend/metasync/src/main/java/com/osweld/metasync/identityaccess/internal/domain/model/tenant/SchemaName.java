package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.util.regex.Pattern;

public record SchemaName(String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    public SchemaName {

        if (value == null) {
            throw new IllegalArgumentException("Schema name cannot be null");
        }

        String trimmedName = value.trim().toLowerCase();
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

        value = trimmedName;
    }

    @Override
    public String toString() {
        return value;
    }

}

package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.util.regex.Pattern;

public record SchemaName(String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private static final int MAX_LENGTH = 63;

    private static final String PREFIX = "t_";

    public SchemaName {

        if (value == null) {
            throw new IllegalArgumentException("Schema name cannot be null");
        }

        String trimmedName = value.trim().toLowerCase();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }

        if (trimmedName.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Schema name cannot be longer than " + MAX_LENGTH + " characters");
        }

        if (!trimmedName.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Schema name must start with reserved prefix '" + PREFIX + "'");
        }

        if (!VALID_PATTERN.matcher(trimmedName).matches()) {
            throw new IllegalArgumentException(
                    "Schema name must start with a letter or underscore and can only contain letters, digits, and underscores");
        }

        value = trimmedName;
    }

    public static SchemaName from(TenantAlias alias) {
        String aliasValue = alias.value();
        String sqlSafe = aliasValue.replaceAll("-", "_");
        String fullSchemaName = PREFIX + sqlSafe;
        return new SchemaName(fullSchemaName);
    }

    @Override
    public String toString() {
        return value;
    }

}

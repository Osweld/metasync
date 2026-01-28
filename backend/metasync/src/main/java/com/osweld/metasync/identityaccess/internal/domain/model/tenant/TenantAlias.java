package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.util.regex.Pattern;

public record TenantAlias(String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*$");

    public TenantAlias {

        if (value == null) {
            throw new IllegalArgumentException("Tenant alias cannot be null");
        }

        String trimmedAlias = value.trim().toLowerCase();

        if (trimmedAlias.isEmpty()) {
            throw new IllegalArgumentException("Tenant alias cannot be blank");
        }

        if (trimmedAlias.length() > 50) {
            throw new IllegalArgumentException("Tenant alias cannot be longer than 50 characters");
        }

        if (!VALID_PATTERN.matcher(trimmedAlias).matches()) {
            throw new IllegalArgumentException(
                    "Tenant alias must start with a letter and can only contain letters, digits, and hyphens");
        }

        value = trimmedAlias;
    }

    @Override
    public String toString() {
        return value;
    }

}

package com.osweld.metasync.shared.domain.model.vo;

import java.util.regex.Pattern;

public record TenantName (String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[\\p{L}0-9\\s.,&'-]+$");

    private static final int MAX_LENGTH = 100;

    public TenantName {

        if (value == null) {
            throw new IllegalArgumentException("Tenant name cannot be null");
        }

        String trimmedName = value.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Tenant name cannot be blank");
        }

        if (trimmedName.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Tenant name cannot be longer than 100 characters");
        }

        if (!VALID_PATTERN.matcher(trimmedName).matches()) {
            throw new IllegalArgumentException(
                    "Tenant name contains invalid characters");
        }

        value = trimmedName;
    }

    @Override
    public String toString() {
        return value;
    }

}
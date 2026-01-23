package com.osweld.metasync.tenant.internal.domain.model.tenant;

import java.util.regex.Pattern;

public final class TenantName {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[\\p{L}0-9\\s.,&'-]+$");

    private final String tenantName;

    public TenantName(String tenantName) {

        if (tenantName == null) {
            throw new IllegalArgumentException("Tenant name cannot be null");
        }

        String trimmedName = tenantName.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Tenant name cannot be blank");
        }

        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("Tenant name cannot be longer than 100 characters");
        }

        if (!VALID_PATTERN.matcher(trimmedName).matches()) {
            throw new IllegalArgumentException(
                    "Tenant name contains invalid characters");
        }

        this.tenantName = trimmedName;
    }

    public String value() {
        return tenantName;
    }

    @Override
    public int hashCode() {
        return tenantName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TenantName that = (TenantName) obj;
        return tenantName.equals(that.tenantName);
    }

    @Override
    public String toString() {
        return tenantName;
    }

}
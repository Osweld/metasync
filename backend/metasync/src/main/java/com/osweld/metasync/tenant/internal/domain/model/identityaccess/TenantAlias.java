package com.osweld.metasync.tenant.internal.domain.model.identityaccess;

import java.util.regex.Pattern;

public final class TenantAlias {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*$");

    private final String tenantAlias;

    public TenantAlias(String tenantAlias) {

        if (tenantAlias == null) {
            throw new IllegalArgumentException("Tenant alias cannot be null");
        }

        String trimmedAlias = tenantAlias.trim().toLowerCase();

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

        this.tenantAlias = trimmedAlias;
    }

    public String value() {
        return tenantAlias;
    }

    @Override
    public int hashCode() {
        return tenantAlias.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TenantAlias that = (TenantAlias) obj;
        return tenantAlias.equals(that.tenantAlias);
    }

    @Override
    public String toString() {
        return tenantAlias;
    }

}

package com.osweld.metasync.identityaccess.domain.model.tenant;

import java.util.UUID;

public record TenantId(UUID value) {

    public TenantId {

        if (value == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
    }

    public static TenantId generate() {
        return new TenantId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

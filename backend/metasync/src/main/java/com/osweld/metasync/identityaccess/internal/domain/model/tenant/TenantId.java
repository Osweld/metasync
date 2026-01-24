package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.util.UUID;

public final class TenantId {

    private final UUID tenantId;

    public TenantId(UUID tenantId) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        this.tenantId = tenantId;
    }

    public static TenantId generate() {
        return new TenantId(UUID.randomUUID());
    }

    public UUID value() {
        return tenantId;
    }

    @Override
    public int hashCode() {
        return tenantId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TenantId that = (TenantId) obj;
        return tenantId.equals(that.tenantId);
    }

    @Override
    public String toString() {
        return tenantId.toString();
    }
}

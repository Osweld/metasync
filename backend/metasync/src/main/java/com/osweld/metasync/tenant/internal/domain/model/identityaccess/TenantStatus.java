package com.osweld.metasync.tenant.internal.domain.model.identityaccess;

public final class TenantStatus {

    private final StatusType statusType;

    public TenantStatus(StatusType statusType) {
        if (statusType == null) {
            throw new IllegalArgumentException("Status type cannot be null");
        }
        this.statusType = statusType;
    }

    public StatusType value() {
        return statusType;
    }

    @Override
    public int hashCode() {
        return statusType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TenantStatus that = (TenantStatus) obj;
        return statusType == that.statusType;
    }

    @Override
    public String toString() {
        return statusType.toString();
    }

}

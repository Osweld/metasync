package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

public record TenantStatus(StatusType value) {

    public TenantStatus {
        if (value == null) {
            throw new IllegalArgumentException("Status type cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

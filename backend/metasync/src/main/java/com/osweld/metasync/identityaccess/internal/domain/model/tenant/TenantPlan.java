package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

public record TenantPlan(PlanType value) {

    public TenantPlan{
        if (value == null) {
            throw new IllegalArgumentException("Plan type cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

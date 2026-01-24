package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

public final class TenantPlan {

    private final PlanType planType;

    public TenantPlan(PlanType planType) {
        if (planType == null) {
            throw new IllegalArgumentException("Plan type cannot be null");
        }
        this.planType = planType;
    }

    public PlanType value() {
        return planType;
    }

    @Override
    public int hashCode() {
        return planType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TenantPlan that = (TenantPlan) obj;
        return planType == that.planType;
    }

    @Override
    public String toString() {
        return planType.toString();
    }

}

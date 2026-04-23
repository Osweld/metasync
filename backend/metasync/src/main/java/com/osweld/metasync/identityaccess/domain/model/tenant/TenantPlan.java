package com.osweld.metasync.identityaccess.domain.model.tenant;

public record TenantPlan(PlanType value) {

    public TenantPlan{
        if (value == null) {
            throw new IllegalArgumentException("Plan type cannot be null");
        }
    }

    public static TenantPlan fromString(String planName) {

        if(planName == null || planName.isBlank()) {
            throw new IllegalArgumentException("Plan name cannot be null or blank");
        }

        try {
            PlanType planType = PlanType.valueOf(planName.trim().toUpperCase());
            return new TenantPlan(planType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plan name: " + planName);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

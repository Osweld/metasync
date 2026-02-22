package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TenantPlanTest {


    @Test
    @DisplayName("Should create a valid TenantPlan")
    void testCreateValidTenantPlan() {
        TenantPlan tenantPlan = new TenantPlan(PlanType.FREE);
        assertThat(tenantPlan.value()).isEqualTo(PlanType.FREE);
    }

    @Test
    @DisplayName("Should consider two TenantPlans with the same value as equal")
    void testTenantPlanEquality() {
        TenantPlan tenantPlan1 = new TenantPlan(PlanType.FREE);
        TenantPlan tenantPlan2 = new TenantPlan(PlanType.FREE);
        assertThat(tenantPlan1).isEqualTo(tenantPlan2);
    }

    @Test
    @DisplayName("Should consider two TenantPlans with different values as unequal")
    void testTenantPlanInequality() {
        TenantPlan freePlan = new TenantPlan(PlanType.FREE);
        TenantPlan premiumPlan = new TenantPlan(PlanType.PREMIUM);
        
        assertThat(freePlan).isNotEqualTo(premiumPlan);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null TenantPlan")
    void testTenantPlanNull() {
        assertThatThrownBy(() -> new TenantPlan(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return String representation of TenantPlan")
    void testTenantPlanToString() {
        TenantPlan tenantPlan = new TenantPlan(PlanType.PREMIUM);
        assertThat(tenantPlan.toString()).isEqualTo("PREMIUM");
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantPlans")
    void testHashCodeConsistency() {
        TenantPlan plan1 = new TenantPlan(PlanType.PLUS);
        TenantPlan plan2 = new TenantPlan(PlanType.PLUS);
        
        assertThat(plan1.hashCode()).isEqualTo(plan2.hashCode());
    }
}

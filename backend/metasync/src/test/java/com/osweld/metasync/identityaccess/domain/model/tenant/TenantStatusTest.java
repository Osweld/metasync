package com.osweld.metasync.identityaccess.domain.model.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.identityaccess.domain.model.tenant.StatusType;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TenantStatusTest {


    @Test
    @DisplayName("Should create a valid TenantStatus")
    void testCreateValidTenantStatus() {
        TenantStatus tenantStatus = new TenantStatus(StatusType.ACTIVE);
        assertThat(tenantStatus.value()).isEqualTo(StatusType.ACTIVE);  
    }

    @Test
    @DisplayName("Should consider two TenantStatuses with the same value as equal")
    void testTenantStatusEquality() {
        TenantStatus tenantStatus1 = new TenantStatus(StatusType.INACTIVE);
        TenantStatus tenantStatus2 = new TenantStatus(StatusType.INACTIVE);
        assertThat(tenantStatus1).isEqualTo(tenantStatus2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null TenantStatus")
    void testTenantStatusNull() {
        assertThatThrownBy(() -> new TenantStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should consider two TenantStatuses with different values as unequal")
    void testTenantStatusInequality() {
        TenantStatus activeStatus = new TenantStatus(StatusType.ACTIVE);
        TenantStatus inactiveStatus = new TenantStatus(StatusType.INACTIVE);
        assertThat(activeStatus).isNotEqualTo(inactiveStatus);
    }

    @Test
    @DisplayName("Should return String representation of TenantStatus")
    void testTenantStatusToString() {
        TenantStatus tenantStatus = new TenantStatus(StatusType.ACTIVE);
        assertThat(tenantStatus.toString()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should have same hashCode for equal TenantStatuses")
    void testHashCodeConsistency() {
        TenantStatus status1 = new TenantStatus(StatusType.INACTIVE);
        TenantStatus status2 = new TenantStatus(StatusType.INACTIVE);
        assertThat(status1.hashCode()).isEqualTo(status2.hashCode());
    }
}

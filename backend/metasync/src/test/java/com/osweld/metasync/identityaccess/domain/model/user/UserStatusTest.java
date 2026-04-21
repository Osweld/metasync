package com.osweld.metasync.identityaccess.internal.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserStatusTest {

    private final static StatusType ACTIVE_STATUS = StatusType.ACTIVE;
    private final static StatusType INACTIVE_STATUS = StatusType.DEACTIVATED;

    @Test
    @DisplayName("Should create a valid UserStatus")
    void testCreateValidUserStatus() {
        UserStatus userStatus = new UserStatus(ACTIVE_STATUS);
        assertThat(userStatus.value()).isEqualTo(ACTIVE_STATUS);
    }


    @Test
    @DisplayName("Should consider two UserStatus with the same value as equal")
    void testUserStatusEquality() {
        UserStatus userStatus1 = new UserStatus(ACTIVE_STATUS);
        UserStatus userStatus2 = new UserStatus(ACTIVE_STATUS);
        assertThat(userStatus1).isEqualTo(userStatus2);
    }

    @Test
    @DisplayName("Should consider two UserStatus with different values as unequal")
    void testUserStatusInequality() {
        UserStatus activeStatus = new UserStatus(ACTIVE_STATUS);
        UserStatus inactiveStatus = new UserStatus(INACTIVE_STATUS);
        assertThat(activeStatus).isNotEqualTo(inactiveStatus);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null UserStatus")
    void testUserStatusNull() {
        assertThatThrownBy(() -> new UserStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return String representation of UserStatus")
    void testUserStatusToString() {
        UserStatus userStatus = new UserStatus(ACTIVE_STATUS);
        assertThat(userStatus.toString()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should have same hashCode for equal UserStatus")
    void testHashCodeConsistency() {
        UserStatus status1 = new UserStatus(ACTIVE_STATUS);
        UserStatus status2 = new UserStatus(ACTIVE_STATUS);
        assertThat(status1.hashCode()).isEqualTo(status2.hashCode());
    }
}

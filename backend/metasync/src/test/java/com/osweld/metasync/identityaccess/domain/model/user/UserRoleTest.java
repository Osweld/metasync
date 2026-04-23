package com.osweld.metasync.identityaccess.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRoleTest {

    private static final Role ADMIN_ROLE = Role.ADMIN;
    private static final Role MEMBER_ROLE = Role.MEMBER;

    @Test
    @DisplayName("Should create a valid UserRole")
    void testCreateValidUserRole() {
        UserRole userRole = new UserRole(ADMIN_ROLE);
        assertThat(userRole.value()).isEqualTo(ADMIN_ROLE);
    }

    @Test
    @DisplayName("Should grant permission based on role")
    void testGrantsPermission() {
        UserRole adminRole = new UserRole(ADMIN_ROLE);
        assertThat(adminRole.grantsPermission(Permission.INVENTORY_WRITE)).isTrue();
        assertThat(adminRole.grantsPermission(Permission.BILLING_READ)).isFalse();
    }

    @Test
    @DisplayName("Should consider two UserRoles with the same value as equal")
    void testUserRoleEquality() {
        UserRole userRole1 = new UserRole(ADMIN_ROLE);
        UserRole userRole2 = new UserRole(ADMIN_ROLE);
        assertThat(userRole1).isEqualTo(userRole2);
    }

    @Test
    @DisplayName("Should consider two UserRoles with different values as unequal")
    void testUserRoleInequality() {
        UserRole adminRole = new UserRole(ADMIN_ROLE);
        UserRole memberRole = new UserRole(MEMBER_ROLE);
        assertThat(adminRole).isNotEqualTo(memberRole);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null UserRole")
    void testUserRoleNull() {
        assertThatThrownBy(() -> new UserRole(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return String representation of UserRole")
    void testUserRoleToString() {
        UserRole userRole = new UserRole(ADMIN_ROLE);
        assertThat(userRole.toString()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Should have same hashCode for equal UserRoles")
    void testHashCodeConsistency() {
        UserRole role1 = new UserRole(ADMIN_ROLE);
        UserRole role2 = new UserRole(ADMIN_ROLE);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
    }
}

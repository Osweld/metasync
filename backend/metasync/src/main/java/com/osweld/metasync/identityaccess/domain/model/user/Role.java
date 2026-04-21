package com.osweld.metasync.identityaccess.internal.domain.model.user;

import java.util.Set;

public enum Role {
    TENANT_OWNER(Set.of(Permission.values())), 
    ADMIN(Set.of(
            Permission.INVENTORY_READ,
            Permission.INVENTORY_WRITE,
            Permission.INVENTORY_DELETE)),
    MEMBER(Set.of(
            Permission.INVENTORY_READ,
            Permission.INVENTORY_WRITE,
            Permission.BILLING_WRITE)),
    GUEST(Set.of(
            Permission.INVENTORY_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean grantsPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
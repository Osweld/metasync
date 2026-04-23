package com.osweld.metasync.identityaccess.domain.model.user;

public enum Permission {

    // INVENTORY
    INVENTORY_READ,
    INVENTORY_WRITE,
    INVENTORY_DELETE,
    // BILLING
    BILLING_READ,
    BILLING_WRITE,
    // TENANT MANAGEMENT
    TENANT_MANAGE_USERS,
    TENANT_MANAGE_PLAN,
    TENANT_MANAGE_SETTINGS,

}

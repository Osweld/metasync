package com.osweld.metasync.identityaccess.domain.model.user;

public record UserRole(Role value) {
    
    public UserRole {
        if (value == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }

    public boolean grantsPermission(Permission permission) {
        return value.grantsPermission(permission);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

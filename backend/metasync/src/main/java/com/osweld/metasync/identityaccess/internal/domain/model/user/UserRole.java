package com.osweld.metasync.identityaccess.internal.domain.model.user;

public record UserRole(Role value) {
    
    public UserRole {
        if (value == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

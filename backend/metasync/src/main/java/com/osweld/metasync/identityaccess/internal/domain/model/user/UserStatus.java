package com.osweld.metasync.identityaccess.internal.domain.model.user;

public record UserStatus(StatusType value) {

    public UserStatus {
        if (value == null) {
            throw new IllegalArgumentException("User status cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

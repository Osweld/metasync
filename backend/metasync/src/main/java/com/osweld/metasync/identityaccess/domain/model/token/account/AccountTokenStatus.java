package com.osweld.metasync.identityaccess.domain.model.token.account;

public record AccountTokenStatus(TokenStatus value) {

    public AccountTokenStatus {
        if (value == null) {
            throw new IllegalArgumentException("Account token status cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

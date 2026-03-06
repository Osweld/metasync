package com.osweld.metasync.identityaccess.internal.domain.model.token.account;


public record TokenHash(String value) {

    public TokenHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TokenHash value must not be null or blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}

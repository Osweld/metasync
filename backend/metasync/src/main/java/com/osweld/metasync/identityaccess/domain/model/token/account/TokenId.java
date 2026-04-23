package com.osweld.metasync.identityaccess.domain.model.token.account;

import java.util.UUID;

public record TokenId(UUID value) {

    public TokenId {
        if (value == null) {
            throw new IllegalArgumentException("TokenId value must not be null");
        }
    }

    public static TokenId generate() {
        return new TokenId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

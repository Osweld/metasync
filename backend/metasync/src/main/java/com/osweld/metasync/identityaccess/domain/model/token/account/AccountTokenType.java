package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

public record AccountTokenType(TokenType value) {

    public AccountTokenType {
        if (value == null) {
            throw new IllegalArgumentException("Account token type cannot be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

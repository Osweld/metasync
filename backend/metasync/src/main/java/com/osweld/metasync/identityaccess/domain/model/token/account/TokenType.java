package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

import lombok.Getter;

@Getter
public enum TokenType {
    PASSWORD_RESET(15L),
    EMAIL_VERIFICATION(60L),
    TENANT_ACTIVATION(60L);

    private final Long expirationMinutes;

    TokenType(Long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }


}

package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

import java.util.regex.Pattern;

public record TokenHash(String value) {

    private static final int SHA256_HEX_LENGTH = 64;
    private static final Pattern SHA256_HEX_PATTERN = Pattern.compile("^[a-f0-9]{64}$");

    public TokenHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TokenHash value must not be null or blank");
        }

        if (value.length() != SHA256_HEX_LENGTH) {
            throw new IllegalArgumentException("TokenHash must be exactly 64 characters");
        }

        if (!SHA256_HEX_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("TokenHash must be a lowercase hexadecimal SHA-256 value");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}

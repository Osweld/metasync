package com.osweld.metasync.identityaccess.domain.model.user;

public record EncryptedPassword(
    String value
) {

    private static final int MIN_LENGTH = 60;
    private static final int MAX_LENGTH = 128;

    public EncryptedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters long");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Password cannot be longer than " + MAX_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return "EncryptedPassword[PROTECTED]";
    }

}

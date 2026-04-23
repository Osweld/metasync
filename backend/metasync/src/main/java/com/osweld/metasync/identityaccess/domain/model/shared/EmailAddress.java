package com.osweld.metasync.identityaccess.domain.model.shared;

import java.util.regex.Pattern;

public record EmailAddress(
        String value
) {

    private static final int MAX_LENGTH = 254;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or blank");
        }

        String trimmedEmail = value.trim().toLowerCase();

        if (trimmedEmail.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Email address cannot be longer than " + MAX_LENGTH + " characters");
        }

        if (!PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email address format");
        }

        value = trimmedEmail;
    }

    @Override
    public String toString() {
        return value;
    }

}

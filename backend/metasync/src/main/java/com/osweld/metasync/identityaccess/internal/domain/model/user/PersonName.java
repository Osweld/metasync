package com.osweld.metasync.identityaccess.internal.domain.model.user;

import java.util.regex.Pattern;

public record PersonName(
    String firstName,
    String lastName
) {

    private static final int MAX_NAME_LENGTH = 100;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z '-]+$");

    public PersonName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        firstName = firstName.trim();
        lastName = lastName.trim();

        if (firstName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("First name cannot be longer than " + MAX_NAME_LENGTH + " characters");
        }
        if (lastName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Last name cannot be longer than " + MAX_NAME_LENGTH + " characters");
        }

        if (!NAME_PATTERN.matcher(firstName).matches()) {
            throw new IllegalArgumentException("First name contains invalid characters");
        }
        if (!NAME_PATTERN.matcher(lastName).matches()) {
            throw new IllegalArgumentException("Last name contains invalid characters");
        }
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}

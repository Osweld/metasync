package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import java.text.Normalizer;
import java.util.regex.Pattern;

public record TenantAlias(String value) {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*$");

    private static final int MAX_LENGTH = 50;

    public TenantAlias {

        if (value == null) {
            throw new IllegalArgumentException("Tenant alias cannot be null");
        }

        String trimmedAlias = value.trim().toLowerCase();

        if (trimmedAlias.isEmpty()) {
            throw new IllegalArgumentException("Tenant alias cannot be blank");
        }

        if (trimmedAlias.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Tenant alias cannot be longer than " + MAX_LENGTH + " characters");
        }

        if (!VALID_PATTERN.matcher(trimmedAlias).matches()) {
            throw new IllegalArgumentException(
                    "Tenant alias must start with a letter and can only contain letters, digits, and hyphens");
        }

        value = trimmedAlias;
    }

    public static TenantAlias derivateFrom(TenantName tenantName) {
        String lowerCase = tenantName.value().toLowerCase();
        String normalized = Normalizer.normalize(lowerCase, Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String sanitized = noAccents.replaceAll("[^a-z0-9]", "-");
        String clean = sanitized.replaceAll("-+","-").replaceAll("^-|-$", "");
        return new TenantAlias(clean);
    }

    @Override
    public String toString() {
        return value;
    }

}

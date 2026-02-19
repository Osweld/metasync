package com.osweld.metasync.identityaccess.internal.domain.model.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;

public class EmailAddressTest {

    private static final String VALID_EMAIL = "test@example.com";

    @Test
    @DisplayName("Should create a valid EmailAddress")
    void testCreateValidEmailAddress() {
        EmailAddress emailAddress = new EmailAddress(VALID_EMAIL);
        assertThat(emailAddress.value()).isEqualTo(VALID_EMAIL);
    }

    @Test
    @DisplayName("Should consider two EmailAddresses with the same value as equal")
    void testEmailAddressEquality() {
        EmailAddress emailAddress1 = new EmailAddress(VALID_EMAIL);
        EmailAddress emailAddress2 = new EmailAddress(VALID_EMAIL);
        assertThat(emailAddress1).isEqualTo(emailAddress2);
    }

    @Test
    @DisplayName("Should consider two EmailAddresses with different values as unequal")
    void testEmailAddressInequality() {
        EmailAddress emailAddress1 = new EmailAddress("test1@example.com");
        EmailAddress emailAddress2 = new EmailAddress("test2@example.com");
        assertThat(emailAddress1).isNotEqualTo(emailAddress2);
    }

    @Test
    @DisplayName("Should trim spaces from EmailAddress")
    void testEmailAddressTrimming() {
        EmailAddress emailAddress = new EmailAddress("  " + VALID_EMAIL + "  ");
        assertThat(emailAddress.value()).isEqualTo(VALID_EMAIL);
    }

    @Test
    @DisplayName("Should convert EmailAddress to lowercase")
    void testEmailAddressLowercase() {
        EmailAddress emailAddress = new EmailAddress("TEST@EXAMPLE.COM");
        assertThat(emailAddress.value()).isEqualTo("test@example.com");
    }

    @ParameterizedTest(name = "Invalid email \"{0}\" should throw exception")
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "   ",
            "invalid-email",
            "user@.com",
            "user@com",
            "user@domain..com"
    })
    @DisplayName("Should reject invalid EmailAddresses")
    void should_reject_invalid_email_addresses(String invalidEmail) {
        assertThatThrownBy(() -> new EmailAddress(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("Should reject EmailAddress longer than max length")
    void should_reject_email_exceeding_max_length() {
        String longEmail = "a".repeat(255 - "@example.com".length());
        String invalidEmail = longEmail + "@example.com";
        assertThatThrownBy(() -> new EmailAddress(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should accept EmailAddress at maximum length")
    void should_accept_email_at_max_length() {
        String localPart = "a".repeat(254 - "@example.com".length());
        String maxEmail = localPart + "@example.com";
        EmailAddress emailAddress = new EmailAddress(maxEmail);
        assertThat(emailAddress.value()).isEqualTo(maxEmail);
    }

    @Test
    @DisplayName("toString should return the email value")
    void testEmailAddressToString() {
        EmailAddress emailAddress = new EmailAddress(VALID_EMAIL);
        assertThat(emailAddress.toString()).isEqualTo(VALID_EMAIL);
    }

    @Test
    @DisplayName("Should have same hashCode for equal EmailAddresses")
    void testHashCodeConsistency() {
        EmailAddress email1 = new EmailAddress(VALID_EMAIL);
        EmailAddress email2 = new EmailAddress(VALID_EMAIL);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

}

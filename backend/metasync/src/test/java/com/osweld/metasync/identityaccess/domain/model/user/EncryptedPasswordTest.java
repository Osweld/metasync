package com.osweld.metasync.identityaccess.internal.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class EncryptedPasswordTest {

    private static final String VALID_ENCRYPTED_PASSWORD = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+[]{}|;:',.<>?/`~";

    @Test
    @DisplayName("Should create a valid EncryptedPassword")
    void testCreateValidEncryptedPassword() {
        EncryptedPassword encryptedPassword = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        assertThat(encryptedPassword.value()).isEqualTo(VALID_ENCRYPTED_PASSWORD);
    }

    @Test
    @DisplayName("Should consider two EncryptedPasswords with the same value as equal")
    void testEncryptedPasswordEquality() {
        EncryptedPassword encryptedPassword1 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        EncryptedPassword encryptedPassword2 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        assertThat(encryptedPassword1).isEqualTo(encryptedPassword2);
    }

    @Test
    @DisplayName("Should consider two EncryptedPasswords with different values as unequal")
    void testEncryptedPasswordInequality() {
        EncryptedPassword encryptedPassword1 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        EncryptedPassword encryptedPassword2 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD + "extra");
        assertThat(encryptedPassword1).isNotEqualTo(encryptedPassword2);
    }

    @ParameterizedTest(name = "Invalid encrypted password \"{0}\" should throw exception")
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "   ",
            "short",
            "thispasswordiswaytoolongthispasswordiswaytoolongthispasswordiswaytoolongthispasswordiswaytoolongthispasswordiswaytoolongthispasswordiswaytoolong"
    })
    void testInvalidEncryptedPasswords(String invalidPassword) {
        assertThatThrownBy(() -> new EncryptedPassword(invalidPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toString should not expose the actual password value")
    void testToStringProtection() {
        EncryptedPassword encryptedPassword = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        assertThat(encryptedPassword.toString()).isEqualTo("EncryptedPassword[PROTECTED]");
    }

    @Test
    @DisplayName("Should have same hashCode for equal EncryptedPasswords")
    void testHashCodeConsistency() {
        EncryptedPassword password1 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        EncryptedPassword password2 = new EncryptedPassword(VALID_ENCRYPTED_PASSWORD);
        assertThat(password1.hashCode()).isEqualTo(password2.hashCode());
    }

}
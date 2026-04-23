package com.osweld.metasync.identityaccess.domain.model.token.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AccountTokenType Tests")
public class AccountTokenTypeTest {

    @Test
    @DisplayName("Should create a valid AccountTokenType")
    void testCreateValidAccountTokenType() {
        AccountTokenType tokenType = new AccountTokenType(TokenType.TENANT_ACTIVATION);
        assertThat(tokenType.value()).isEqualTo(TokenType.TENANT_ACTIVATION);
    }

    @Test
    @DisplayName("Should consider two AccountTokenTypes with the same value as equal")
    void testAccountTokenTypeEquality() {
        AccountTokenType tokenType1 = new AccountTokenType(TokenType.PASSWORD_RESET);
        AccountTokenType tokenType2 = new AccountTokenType(TokenType.PASSWORD_RESET);
        assertThat(tokenType1).isEqualTo(tokenType2);
    }

    @Test
    @DisplayName("Should consider two AccountTokenTypes with different values as unequal")
    void testAccountTokenTypeInequality() {
        AccountTokenType tokenType1 = new AccountTokenType(TokenType.PASSWORD_RESET);
        AccountTokenType tokenType2 = new AccountTokenType(TokenType.EMAIL_VERIFICATION);
        assertThat(tokenType1).isNotEqualTo(tokenType2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null AccountTokenType")
    void testAccountTokenTypeNull() {
        assertThatThrownBy(() -> new AccountTokenType(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account token type cannot be null");
    }

    @Test
    @DisplayName("Should return String representation of AccountTokenType")
    void testAccountTokenTypeToString() {
        AccountTokenType tokenType = new AccountTokenType(TokenType.PASSWORD_RESET);
        assertThat(tokenType.toString()).isEqualTo("PASSWORD_RESET");
    }

    @Test
    @DisplayName("Should have same hashCode for equal AccountTokenTypes")
    void testHashCodeConsistency() {
        AccountTokenType tokenType1 = new AccountTokenType(TokenType.EMAIL_VERIFICATION);
        AccountTokenType tokenType2 = new AccountTokenType(TokenType.EMAIL_VERIFICATION);
        assertThat(tokenType1.hashCode()).isEqualTo(tokenType2.hashCode());
    }
}

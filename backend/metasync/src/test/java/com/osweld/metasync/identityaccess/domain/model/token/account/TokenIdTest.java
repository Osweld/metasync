package com.osweld.metasync.identityaccess.domain.model.token.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.identityaccess.domain.model.token.account.TokenId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TokenId Tests")
public class TokenIdTest {

    @Test
    @DisplayName("Should create a valid TokenId")
    void testCreateValidTokenId() {
        TokenId tokenId = TokenId.generate();
        assertThat(tokenId).isNotNull();
    }

    @Test
    @DisplayName("Should consider two TokenIds with the same value as equal")
    void testTokenIdEquality() {
        TokenId tokenId1 = TokenId.generate();
        TokenId tokenId2 = new TokenId(tokenId1.value());
        assertThat(tokenId1).isEqualTo(tokenId2);
    }

    @Test
    @DisplayName("Should consider two TokenIds with different values as unequal")
    void testTokenIdInequality() {
        TokenId tokenId1 = TokenId.generate();
        TokenId tokenId2 = TokenId.generate();
        assertThat(tokenId1).isNotEqualTo(tokenId2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null TokenId value")
    void testTokenIdNullValue() {
        assertThatThrownBy(() -> new TokenId(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return string representation of UUID")
    void testTokenIdStringValue() {
        TokenId tokenId = TokenId.generate();
        assertThat(tokenId.value().toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Should have same hash code for equal TokenIds")
    void testTokenIdHashCode() {
        TokenId tokenId1 = TokenId.generate();
        TokenId tokenId2 = new TokenId(tokenId1.value());
        assertThat(tokenId1.hashCode()).isEqualTo(tokenId2.hashCode());
    }

}

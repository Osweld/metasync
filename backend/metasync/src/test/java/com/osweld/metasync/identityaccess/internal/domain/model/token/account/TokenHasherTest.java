package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TokenHasher Tests")
public class TokenHasherTest {

    @Test
    @DisplayName("Should hash canonical UUID with known SHA-256 value")
    void shouldHashCanonicalUuidWithKnownSha256Value() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";

        String hash = TokenHasher.hashToken(uuid);

        assertThat(hash)
            .isEqualTo("a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34");
    }

    @Test
    @DisplayName("Should produce deterministic hash for same UUID")
    void shouldProduceDeterministicHashForSameUuid() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";

        String hash1 = TokenHasher.hashToken(uuid);
        String hash2 = TokenHasher.hashToken(uuid);

        assertThat(hash1)
            .isEqualTo(hash2);
    }

    @Test
    @DisplayName("Should produce different hashes for different UUIDs")
    void shouldProduceDifferentHashesForDifferentUuids() {
        String uuid1 = "550e8400-e29b-41d4-a716-446655440000";
        String uuid2 = "550e8400-e29b-41d4-a716-446655440001";

        String hash1 = TokenHasher.hashToken(uuid1);
        String hash2 = TokenHasher.hashToken(uuid2);

        assertThat(hash1)
            .isNotEqualTo(hash2);
    }

    @Test
    @DisplayName("Should return lowercase hexadecimal SHA-256 hash for UUID")
    void shouldReturnLowercaseHexSha256ForUuid() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";

        String hash = TokenHasher.hashToken(uuid);

        assertThat(hash)
            .isNotNull()
            .isNotBlank()
            .hasSize(64)
            .matches("[a-f0-9]{64}");
    }

    @Test
    @DisplayName("Should treat uppercase and lowercase UUID as different input")
    void shouldTreatUppercaseAndLowercaseUuidAsDifferentInput() {
        String lower = "550e8400-e29b-41d4-a716-446655440000";
        String upper = "550E8400-E29B-41D4-A716-446655440000";

        String lowerHash = TokenHasher.hashToken(lower);
        String upperHash = TokenHasher.hashToken(upper);

        assertThat(lowerHash) //printf '%s' '550e8400-e29b-41d4-a716-446655440000' | sha256sum
            .isEqualTo("a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34");

        assertThat(upperHash)
            .isEqualTo("c44ca770e1e288feee9bedaf9ccea1b4eb3f20c92d75321fa12a2e8919273b67");

        assertThat(lowerHash)
            .isNotEqualTo(upperHash);
    }

    @Test
    @DisplayName("Should throw exception when UUID is null")
    void shouldThrowExceptionWhenUuidIsNull() {
        assertThatThrownBy(() -> TokenHasher.hashToken(null))
            .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "550e8400-e29b-41d4-a716-446655440000",
        "123e4567-e89b-12d3-a456-426614174000",
        "00000000-0000-0000-0000-000000000000",
        "ffffffff-ffff-ffff-ffff-ffffffffffff"
    })
    @DisplayName("Should hash UUID values used as account activation tokens")
    void shouldHashUuidValuesUsedAsAccountActivationTokens(String uuid) {
        String hash = TokenHasher.hashToken(uuid);

        assertThat(hash)
            .isNotNull()
            .hasSize(64)
            .matches("[a-f0-9]{64}");
    }

    @Test
    @DisplayName("Should not collide in a small UUID sample")
    void shouldNotCollideInSmallUuidSample() {
        List<String> uuids = List.of(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        List<String> hashes = uuids.stream()
            .map(TokenHasher::hashToken)
            .toList();

        assertThat(hashes)
            .hasSize(uuids.size())
            .doesNotHaveDuplicates();
    }
}

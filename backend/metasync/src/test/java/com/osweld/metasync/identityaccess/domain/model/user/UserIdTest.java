package com.osweld.metasync.identityaccess.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserIdTest {

    private static final UUID VALID_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    @DisplayName("Should create a valid UserId")
    void testCreateValidUserId() {
        UserId userId = new UserId(VALID_UUID);
        assertThat(userId.value()).isEqualTo(VALID_UUID);
    }

    @Test
    @DisplayName("Should generate a new UserId")
    void testGenerateUserId() {
        UserId userId = UserId.generate();
        assertThat(userId.value()).isNotNull();
    }

    @Test
    @DisplayName("Should consider two UserIds with the same value as equal")
    void testUserIdEquality() {
        UserId userId1 = new UserId(VALID_UUID);
        UserId userId2 = new UserId(VALID_UUID);
        assertThat(userId1).isEqualTo(userId2);
    }

    @Test
    @DisplayName("Should consider two UserIds with different values as unequal")
    void testUserIdInequality() {
        UserId userId1 = new UserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        UserId userId2 = new UserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        assertThat(userId1).isNotEqualTo(userId2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null UserId")
    void testUserIdIsNull() {
        assertThatThrownBy(() -> new UserId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return String representation of UserId")
    void testUserIdToString() {
        UserId userId = new UserId(VALID_UUID);
        assertThat(userId.toString()).isEqualTo(VALID_UUID.toString());
    }

    @Test
    @DisplayName("Should have same hashCode for equal UserIds")
    void testUserIdHashCode() {
        UserId userId1 = new UserId(VALID_UUID);
        UserId userId2 = new UserId(VALID_UUID);
        assertThat(userId1.hashCode()).isEqualTo(userId2.hashCode());
    }
}

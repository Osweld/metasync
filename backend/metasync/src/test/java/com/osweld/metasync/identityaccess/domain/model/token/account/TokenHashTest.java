package com.osweld.metasync.identityaccess.domain.model.token.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TokenHash Tests")
public class TokenHashTest {

	@Test
	@DisplayName("Should create TokenHash with valid lowercase SHA-256 value")
	void shouldCreateTokenHashWithValidLowercaseSha256Value() {
		String validHash = "a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34";

		TokenHash tokenHash = new TokenHash(validHash);

		assertThat(tokenHash.value()).isEqualTo(validHash);
		assertThat(tokenHash.toString()).isEqualTo(validHash);
	}

	@Test
	@DisplayName("Should throw exception when value is null")
	void shouldThrowExceptionWhenValueIsNull() {
		assertThatThrownBy(() -> new TokenHash(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("TokenHash value must not be null or blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "   ", "\t", "\n"})
	@DisplayName("Should throw exception when value is blank")
	void shouldThrowExceptionWhenValueIsBlank(String value) {
		assertThatThrownBy(() -> new TokenHash(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("TokenHash value must not be null or blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"abc",
		"a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a3",
		"a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a340"
	})
	@DisplayName("Should throw exception when value length is not 64")
	void shouldThrowExceptionWhenValueLengthIsNot64(String value) {
		assertThatThrownBy(() -> new TokenHash(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("TokenHash must be exactly 64 characters");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"A3A9E1ED9732CAB28868127BE00F1CE921ACAEFDD5C3B23A6E9E0072BD9C1A34",
		"g3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34",
		"a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a3-"
	})
	@DisplayName("Should throw exception when value is not lowercase hexadecimal")
	void shouldThrowExceptionWhenValueIsNotLowercaseHexadecimal(String value) {
		assertThatThrownBy(() -> new TokenHash(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("TokenHash must be a lowercase hexadecimal SHA-256 value");
	}
}

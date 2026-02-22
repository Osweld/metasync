package com.osweld.metasync.identityaccess.internal.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PersonNameTest {

    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";

    @Test
    @DisplayName("Should create a valid PersonName")
    void testCreateValidPersonName() {
        PersonName personName = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        assertThat(personName.firstName()).isEqualTo(VALID_FIRST_NAME);
        assertThat(personName.lastName()).isEqualTo(VALID_LAST_NAME);
    }

    @Test
    @DisplayName("Should consider two PersonNames with the same values as equal")
    void testPersonNameEquality() {
        PersonName personName1 = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        PersonName personName2 = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        assertThat(personName1).isEqualTo(personName2);
    }

    @Test
    @DisplayName("Should consider two PersonNames with different values as unequal")
    void testPersonNameInequality() {
        PersonName personName1 = new PersonName("Jane", VALID_LAST_NAME);
        PersonName personName2 = new PersonName(VALID_FIRST_NAME, "Smith");
        assertThat(personName1).isNotEqualTo(personName2);
    }

    @Test
    @DisplayName("Should trim spaces from PersonName")
    void testPersonNameTrimming() {
        PersonName personName = new PersonName("  " + VALID_FIRST_NAME + "  ", "  " + VALID_LAST_NAME + "  ");
        assertThat(personName.firstName()).isEqualTo(VALID_FIRST_NAME);
        assertThat(personName.lastName()).isEqualTo(VALID_LAST_NAME);
    }

    @ParameterizedTest(name = "Invalid first name \"{0}\" should throw exception")
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "   ",
            "$#@!InvalidChars",
            "ThisNameIsWayTooLong....................................................................................",
    })
    void testInvalidFirstNames(String invalidFirstName) {
        assertThatThrownBy(() -> new PersonName(invalidFirstName, VALID_LAST_NAME))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @ParameterizedTest(name = "Invalid last name \"{0}\" should throw exception")
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "   ",
            "$#@!InvalidChars",
            "ThisNameIsWayTooLong....................................................................................",
    })
    void testInvalidLastNames(String invalidLastName) {
        assertThatThrownBy(() -> new PersonName(VALID_FIRST_NAME, invalidLastName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return full name correctly")
    void testFullName() {
        PersonName personName = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        assertThat(personName.fullName()).isEqualTo(VALID_FIRST_NAME + " " + VALID_LAST_NAME);
    }

    @Test
    @DisplayName("Should return string representation correctly")
    void testToString() {
        PersonName personName = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        assertThat(personName.toString()).isEqualTo(VALID_FIRST_NAME + " " + VALID_LAST_NAME);
    }

    @Test
    @DisplayName("Should have same hash code for equal PersonNames")
    void testHashCode() {
        PersonName personName1 = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        PersonName personName2 = new PersonName(VALID_FIRST_NAME, VALID_LAST_NAME);
        assertThat(personName1.hashCode()).isEqualTo(personName2.hashCode());
    }
}

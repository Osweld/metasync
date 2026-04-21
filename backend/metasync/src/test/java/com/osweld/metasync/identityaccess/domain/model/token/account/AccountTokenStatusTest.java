package com.osweld.metasync.identityaccess.domain.model.token.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AccountTokenStatusTest {

    @Test
    @DisplayName("Should create a valid AccountTokenStatus")
    void testCreateValidAccountTokenStatus() {
        AccountTokenStatus status = new AccountTokenStatus(TokenStatus.UNUSED);
        assertNotNull(status);
        assertEquals(TokenStatus.UNUSED, status.value());
    }

}
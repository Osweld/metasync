package com.osweld.metasync.identityaccess.internal.infrastructure.security;


import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;

public class FakeEncryptionService implements EncryptionService {

    private final static String ENCRYPTION_PREFIX = "encrypted-encrypted-encrypted-encrypted-encrypted-encrypted-";

    @Override
    public EncryptedPassword encryptPassword(String plainPassword) {
      return new EncryptedPassword(ENCRYPTION_PREFIX + plainPassword);
    }

    @Override
    public String decryptPassword(EncryptedPassword encryptedPassword) {
        String encryptedValue = encryptedPassword.value();
        if (encryptedValue.startsWith(ENCRYPTION_PREFIX)) {
            return encryptedValue.substring(ENCRYPTION_PREFIX.length());
        }
        throw new IllegalArgumentException("Invalid encrypted password format");
    }

}

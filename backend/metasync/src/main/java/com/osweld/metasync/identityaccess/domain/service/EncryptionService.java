package com.osweld.metasync.identityaccess.domain.service;

import com.osweld.metasync.identityaccess.domain.model.user.EncryptedPassword;

public interface EncryptionService {
    EncryptedPassword encryptPassword(String plainPassword);
    boolean verifyPassword(String plainPassword, EncryptedPassword encryptedPassword);

}

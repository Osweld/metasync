package com.osweld.metasync.identityaccess.internal.domain.service;

import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;

public interface EncryptionService {
    EncryptedPassword encryptPassword(String plainPassword);
    String decryptPassword(EncryptedPassword encryptedPassword);

}

package com.osweld.metasync.identityaccess.infrastructure.security;

import org.springframework.stereotype.Service;

import com.osweld.metasync.identityaccess.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.domain.service.EncryptionService;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class BCryptEncryptionService implements EncryptionService {


    @Override
    public EncryptedPassword encryptPassword(String plainPassword) {
        EncryptedPassword encryptedPassword = new EncryptedPassword(BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray()));
        return encryptedPassword;
    }

    @Override
    public boolean verifyPassword(String plainPassword, EncryptedPassword encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), encryptedPassword.value().toCharArray());
        return result.verified;
    }

    

}

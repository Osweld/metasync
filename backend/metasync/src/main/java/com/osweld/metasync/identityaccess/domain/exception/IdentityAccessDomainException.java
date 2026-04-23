package com.osweld.metasync.identityaccess.domain.exception;

public class IdentityAccessDomainException extends RuntimeException {
    
    public IdentityAccessDomainException(String message) {
        super(message);
    }

    public IdentityAccessDomainException(String message, Throwable cause) {
        super(message, cause);
    }

}

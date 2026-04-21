package com.osweld.metasync.identityaccess.internal.domain.exception;

public class IdentityAccessDomainException extends RuntimeException {
    
    public IdentityAccessDomainException(String message) {
        super(message);
    }

    public IdentityAccessDomainException(String message, Throwable cause) {
        super(message, cause);
    }

}

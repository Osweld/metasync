package com.osweld.metasync.shared.exception.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    int status,
    String error,
    String message,
    Map<String, Object> details,
    LocalDateTime timestamp
) {

     public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null, LocalDateTime.now());
    }

     public ErrorResponse(int status, String error, String message, Map<String, Object> details) {
        this(status, error, message, details, LocalDateTime.now());
    }

}

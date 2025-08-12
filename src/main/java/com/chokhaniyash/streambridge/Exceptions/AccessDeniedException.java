package com.chokhaniyash.streambridge.Exceptions;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends AppException {
    public AccessDeniedException(String message) {
        super(message,"ACCESS_DENIED", HttpStatus.BAD_REQUEST);
    }
}

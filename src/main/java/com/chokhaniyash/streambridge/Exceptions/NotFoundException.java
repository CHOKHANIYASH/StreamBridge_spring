package com.chokhaniyash.streambridge.Exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message){
        super(message,"ITEM_NOT_FOUND",HttpStatus.NOT_FOUND);
    }

}

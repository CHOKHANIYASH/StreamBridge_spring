package com.chokhaniyash.streambridge.Exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends AppException  {
    public AlreadyExistException(String message){
        super(message,"ITEM_ALREADY_EXIST", HttpStatus.CONFLICT);
    }
}

package com.chokhaniyash.streambridge.Exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException{
    private final String type;
    private final HttpStatus status;
    public AppException(String message,String type,HttpStatus status){
        super(message);
        this.type = type;
        this.status = status;
    }
    public String getType(){
        return type;
    }
    public HttpStatus getStatus(){
        return status;
    }
}

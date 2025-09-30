package com.example.springboots.exception;


/**
 * 功能:
 **/
public class ServiceException extends RuntimeException{
    public ServiceException(String message){
        super(message);
    }
}

package com.ets.EmergencyTriageSystem.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String ex){
        super(ex);
    }
}
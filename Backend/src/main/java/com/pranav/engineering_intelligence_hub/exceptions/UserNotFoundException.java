package com.pranav.engineering_intelligence_hub.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(Long id){
        super("User Not found with id "+id);
    }
    public UserNotFoundException(String message){
        super(message);
    }
}

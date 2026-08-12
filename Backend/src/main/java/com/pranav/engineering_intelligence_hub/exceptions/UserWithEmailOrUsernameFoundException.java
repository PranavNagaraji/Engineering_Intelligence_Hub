package com.pranav.engineering_intelligence_hub.exceptions;

public class UserWithEmailOrUsernameFoundException extends RuntimeException{
    public UserWithEmailOrUsernameFoundException(){
        super("User with provided Email or Username already exists!");
    }
}

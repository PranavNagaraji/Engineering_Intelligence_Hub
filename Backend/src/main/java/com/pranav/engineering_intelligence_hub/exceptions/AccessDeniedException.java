package com.pranav.engineering_intelligence_hub.exceptions;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(){
        super("User lacks the authority to perform this action");
    }
    public AccessDeniedException(String message){
        super(message);
    }
}

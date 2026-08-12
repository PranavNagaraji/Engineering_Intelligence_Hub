package com.pranav.engineering_intelligence_hub.exceptions;

public class TeamAlreadyFoundException extends RuntimeException{
    public TeamAlreadyFoundException(String teamName){
        super("Team with name " + teamName + " already exists");
    }
}

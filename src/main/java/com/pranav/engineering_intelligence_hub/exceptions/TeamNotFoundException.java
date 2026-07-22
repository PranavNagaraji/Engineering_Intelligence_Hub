package com.pranav.engineering_intelligence_hub.exceptions;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(Long id){
        super("Team Not found with id "+id);
    }
}

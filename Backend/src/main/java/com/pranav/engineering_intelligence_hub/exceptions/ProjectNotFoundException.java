package com.pranav.engineering_intelligence_hub.exceptions;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(Long id){
        super("Project Not found with id "+id);
    }
}

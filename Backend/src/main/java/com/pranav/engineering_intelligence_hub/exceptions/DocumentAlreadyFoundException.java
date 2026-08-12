package com.pranav.engineering_intelligence_hub.exceptions;

import jakarta.validation.constraints.NotNull;

public class DocumentAlreadyFoundException extends RuntimeException{
    public DocumentAlreadyFoundException(@NotNull String title){
        super("Document with title \""+title+"\" already exists");
    }
}

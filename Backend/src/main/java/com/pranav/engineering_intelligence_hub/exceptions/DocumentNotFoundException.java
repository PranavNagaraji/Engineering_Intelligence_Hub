package com.pranav.engineering_intelligence_hub.exceptions;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(Long documentId) {
        super("Document with id: "+documentId+" not found");
    }
}

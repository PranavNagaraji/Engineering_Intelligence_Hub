package com.pranav.engineering_intelligence_hub.exceptions;

public class IncidentStatusInvalidException extends RuntimeException {
    public IncidentStatusInvalidException() {
        super("Invalid IncidentStatus action performed");
    }
    public IncidentStatusInvalidException(String message) {
        super(message);
    }
}

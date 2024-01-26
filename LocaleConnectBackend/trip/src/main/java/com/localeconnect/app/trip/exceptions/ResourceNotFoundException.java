package com.localeconnect.app.trip.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

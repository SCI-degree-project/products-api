package edu.api.products.application.exceptions;

public class InvalidTenantException extends RuntimeException {
    public InvalidTenantException(String message) {
        super(message);
    }
}

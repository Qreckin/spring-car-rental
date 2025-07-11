package com.example.car.enums;


public class Enums {

    public enum Errors {
        NO_ERROR(0, "Successful"),
        NOT_FOUND(1, "Resource not found"),
        UNAUTHORIZED(2, "Unauthorized access"),
        FORBIDDEN(3, "Forbidden action"),
        VALIDATION_ERROR(4, "Validation failed"),
        INTERNAL_ERROR(5, "Internal server error"),
        BAD_REQUEST(6, "Bad request"),
        DUPLICATE_RESOURCE(7, "Resource already exists"),
        CONFLICT(8, "Conflict occurred"),
        DATABASE_ERROR(9, "Database operation failed"),
        NETWORK_ERROR(10, "Network error"),
        SERVICE_UNAVAILABLE(11, "Service is temporarily unavailable");

        private final int value;
        private final String message;

        Errors(int value, String message) {
            this.value = value;
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }
    }

}

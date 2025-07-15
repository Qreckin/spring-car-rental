package com.example.car.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    public enum GearType {
        AUTOMATIC(0),
        MANUAL(1);

        private final int value;

        GearType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @JsonCreator
        public static GearType fromValue(int value) {
            for (GearType type : GearType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid GearType value: " + value);
        }

        @JsonValue
        public int toValue() {
            return value;
        }
    }

    public enum Status {
        RESERVED(0),
        ACTIVE(1),
        COMPLETED(2),
        CANCELLED(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }

        @JsonCreator
        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid status value: " + value);
        }
    }

}

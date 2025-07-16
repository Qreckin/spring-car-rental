package com.example.car;

import com.example.car.enums.Enums;

public class CustomResponseEntity {

    private Integer status;
    private String message;
    private Object detail;

    // No-args constructor
    public CustomResponseEntity() {
    }

    // All-args constructor
    public CustomResponseEntity(Integer status, String message, Object detail) {
        this.status = status;
        this.message = message;
        this.detail = detail;
    }

    // Constructor with status and message only
    public CustomResponseEntity(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor with base response and additional detail
    public CustomResponseEntity(CustomResponseEntity response, Object result) {
        this.status = response.getStatus();
        this.message = response.getMessage();
        this.detail = result;
    }

    // Static factory method for success response with result
    public static CustomResponseEntity OK(Object result) {
        return new CustomResponseEntity(0, "SUCCESS", result);
    }

    public static CustomResponseEntity BAD_REQUEST(Object result){
        return new CustomResponseEntity(Enums.Errors.BAD_REQUEST.getValue(), "BAD REQUEST", result);
    }

    // Getters and setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    // Optional: toString method (since Lombok's @Data includes it)
    @Override
    public String toString() {
        return "CustomResponseEntity{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", detail=" + detail +
                '}';
    }

    // Static predefined responses
    public static final CustomResponseEntity OK =
            new CustomResponseEntity(Enums.Errors.NO_ERROR.getValue(), Enums.Errors.NO_ERROR.getMessage());

    // new
    public static final CustomResponseEntity RENTAL_NOT_FOUND =
            new CustomResponseEntity(Enums.Errors.NOT_FOUND.getValue(), Enums.Errors.NOT_FOUND.getMessage(), "Rental not found");

    public static final CustomResponseEntity CUSTOMER_NOT_FOUND =
            new CustomResponseEntity(Enums.Errors.NOT_FOUND.getValue(), Enums.Errors.NOT_FOUND.getMessage(), "Customer not found");

    public static final CustomResponseEntity CAR_NOT_FOUND =
            new CustomResponseEntity(Enums.Errors.NOT_FOUND.getValue(), Enums.Errors.NOT_FOUND.getMessage(), "Car not found");



    public static final CustomResponseEntity NOT_FOUND =
            new CustomResponseEntity(Enums.Errors.NOT_FOUND.getValue(), Enums.Errors.NOT_FOUND.getMessage());

    public static final CustomResponseEntity UNAUTHORIZED =
            new CustomResponseEntity(Enums.Errors.UNAUTHORIZED.getValue(), Enums.Errors.UNAUTHORIZED.getMessage());

    public static final CustomResponseEntity FORBIDDEN =
            new CustomResponseEntity(Enums.Errors.FORBIDDEN.getValue(), Enums.Errors.FORBIDDEN.getMessage());

    public static final CustomResponseEntity VALIDATION_ERROR =
            new CustomResponseEntity(Enums.Errors.VALIDATION_ERROR.getValue(), Enums.Errors.VALIDATION_ERROR.getMessage());

    public static final CustomResponseEntity INTERNAL_ERROR =
            new CustomResponseEntity(Enums.Errors.INTERNAL_ERROR.getValue(), Enums.Errors.INTERNAL_ERROR.getMessage());

    public static final CustomResponseEntity BAD_REQUEST =
            new CustomResponseEntity(Enums.Errors.BAD_REQUEST.getValue(), Enums.Errors.BAD_REQUEST.getMessage());

    public static final CustomResponseEntity END_BEFORE_START =
            new CustomResponseEntity(Enums.Errors.BAD_REQUEST.getValue(), Enums.Errors.BAD_REQUEST.getMessage(), "End date must be after start date");

    public static final CustomResponseEntity DUPLICATE_RESOURCE =
            new CustomResponseEntity(Enums.Errors.DUPLICATE_RESOURCE.getValue(), Enums.Errors.DUPLICATE_RESOURCE.getMessage());

    public static final CustomResponseEntity CONFLICT =
            new CustomResponseEntity(Enums.Errors.CONFLICT.getValue(), Enums.Errors.CONFLICT.getMessage());

    // new
    public static final CustomResponseEntity CAR_IS_OCCUPIED =
            new CustomResponseEntity(Enums.Errors.CONFLICT.getValue(), Enums.Errors.CONFLICT.getMessage(), "Car is occupied in this time interval");

    public static final CustomResponseEntity DATABASE_ERROR =
            new CustomResponseEntity(Enums.Errors.DATABASE_ERROR.getValue(), Enums.Errors.DATABASE_ERROR.getMessage());

    public static final CustomResponseEntity NETWORK_ERROR =
            new CustomResponseEntity(Enums.Errors.NETWORK_ERROR.getValue(), Enums.Errors.NETWORK_ERROR.getMessage());

    public static final CustomResponseEntity SERVICE_UNAVAILABLE =
            new CustomResponseEntity(Enums.Errors.SERVICE_UNAVAILABLE.getValue(), Enums.Errors.SERVICE_UNAVAILABLE.getMessage());

    // Inner placeholder enum if you need it
    private enum Errors {
        // Example entries (define real ones as needed)
        ;
    }
}
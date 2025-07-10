package com.example.car.exception;

import com.example.car.enums.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponseEntity {

    private Integer status;
    private String message;
    private Object detail;

    private enum Errors{

    }

    // Static predefined responses
    public static final CustomResponseEntity OK =
            new CustomResponseEntity(Enums.Errors.NO_ERROR.getValue(), Enums.Errors.NO_ERROR.getMessage());

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

    public static final CustomResponseEntity DUPLICATE_RESOURCE =
            new CustomResponseEntity(Enums.Errors.DUPLICATE_RESOURCE.getValue(), Enums.Errors.DUPLICATE_RESOURCE.getMessage());

    public static final CustomResponseEntity CONFLICT =
            new CustomResponseEntity(Enums.Errors.CONFLICT.getValue(), Enums.Errors.CONFLICT.getMessage());

    public static final CustomResponseEntity DATABASE_ERROR =
            new CustomResponseEntity(Enums.Errors.DATABASE_ERROR.getValue(), Enums.Errors.DATABASE_ERROR.getMessage());

    public static final CustomResponseEntity NETWORK_ERROR =
            new CustomResponseEntity(Enums.Errors.NETWORK_ERROR.getValue(), Enums.Errors.NETWORK_ERROR.getMessage());

    public static final CustomResponseEntity SERVICE_UNAVAILABLE =
            new CustomResponseEntity(Enums.Errors.SERVICE_UNAVAILABLE.getValue(), Enums.Errors.SERVICE_UNAVAILABLE.getMessage());



    // Constructor with status and message
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
}
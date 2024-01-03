package com.gestion.contactos.modelo;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T result;
    private int statusCode;

    public ApiResponse() {
        // Constructor por defecto necesario para la serialización/deserialización
    }

    public ApiResponse(boolean success, String message, int statusCode) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ApiResponse(boolean success, String message, T result, int statusCode) {
        this.success = success;
        this.message = message;
        this.result = result;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static <T> ApiResponse<T> success(String message, int statusCode) {
        return new ApiResponse<>(true, message, statusCode);
    }

    public static <T> ApiResponse<T> success(String message, T result, int statusCode) {
        return new ApiResponse<>(true, message, result, statusCode);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, message, statusCode);
    }

    public static <T> ApiResponse<T> error(String message, T result, int statusCode) {
        return new ApiResponse<>(false, message, result, statusCode);
    }
}

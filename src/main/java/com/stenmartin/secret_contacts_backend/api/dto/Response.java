package com.stenmartin.secret_contacts_backend.api.dto;

public class Response<T> {
    private T result;
    private String errorMessage;
    private boolean success;

    public Response(T result, String errorMessage, boolean success) {
        this.result = result;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

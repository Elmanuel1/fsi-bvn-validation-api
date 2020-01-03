package com.splinx.nibss.exceptions;




public class ApplicationException extends Exception {

    private String responseCode;

    public ApplicationException(String responseCode, String message) {
        super(message);
        this.responseCode = responseCode;

    }

    public ApplicationException(String responseCode, String message, Throwable t) {
        super(message, t);
        this.responseCode = responseCode;
    }

    public String getCode() {
        return responseCode;
    }

}

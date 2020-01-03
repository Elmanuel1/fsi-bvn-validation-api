package com.splinx.nibss.exceptions;

public class BadRemoteResponseException extends ApplicationException {


    public BadRemoteResponseException(String code, String message) {
        super(code, message);
    }

    public BadRemoteResponseException(String code, String message, Throwable t) {
        super(code, message);
    }

}

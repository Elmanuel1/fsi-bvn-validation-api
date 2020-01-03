package com.splinx.nibss.exceptions;

public class BadRequestException extends ApplicationException {


    public BadRequestException(String code, String message) {
        super(code, message);
    }

    public BadRequestException(String code, String message, Throwable t) {
        super(code, message);
    }

}

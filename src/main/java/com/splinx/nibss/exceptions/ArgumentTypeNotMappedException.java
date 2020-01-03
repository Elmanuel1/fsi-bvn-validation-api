package com.splinx.nibss.exceptions;

public class ArgumentTypeNotMappedException extends ApplicationException {


    public ArgumentTypeNotMappedException(String code, String message) {
        super(code, message);
    }

    public ArgumentTypeNotMappedException(String code, String message, Throwable t) {
        super(code, message);
    }

}

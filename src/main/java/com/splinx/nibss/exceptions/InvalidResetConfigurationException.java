package com.splinx.nibss.exceptions;

public class InvalidResetConfigurationException extends ApplicationException {


    public InvalidResetConfigurationException(String code, String message) {
        super(code, message);
    }

    public InvalidResetConfigurationException(String code, String message, Throwable t) {
        super(code, message);
    }

}

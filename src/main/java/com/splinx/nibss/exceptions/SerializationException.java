package com.splinx.nibss.exceptions;

public class SerializationException extends ApplicationException {


    public SerializationException(String code, String message) {
        super(code, message);
    }

    public SerializationException(String code, String message, Throwable t) {
        super(code, message);
    }

}

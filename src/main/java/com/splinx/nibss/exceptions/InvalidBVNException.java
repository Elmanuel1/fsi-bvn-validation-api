package com.splinx.nibss.exceptions;

public class InvalidBVNException  extends Exception{

    private String code;
    public InvalidBVNException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

package com.splinx.nibss.exceptions;

public class EncryptionException extends Exception{

    private String code;

    public EncryptionException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    public EncryptionException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

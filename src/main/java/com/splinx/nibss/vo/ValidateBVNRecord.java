package com.splinx.nibss.vo;

public class ValidateBVNRecord extends BVNResponse {

    private String message;
    private ValidateBVNDetail data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidateBVNDetail getData() {
        return data;
    }

    public void setData(ValidateBVNDetail data) {
        this.data = data;
    }
}

package com.splinx.nibss.vo;

import com.splinx.nibss.util.DateFormatter;

public class BaseBVNResponse {

    private String responseCode;
    private String message;
    private String responseTime;
    public String getResponseCode() {
        return this.responseCode;
    }

    public BaseBVNResponse() {
        this.responseTime = DateFormatter.getCurrentTimeString();
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseTime() {
        return responseTime;
    }
}

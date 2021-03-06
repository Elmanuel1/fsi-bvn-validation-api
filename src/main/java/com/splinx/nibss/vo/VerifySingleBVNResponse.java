package com.splinx.nibss.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifySingleBVNResponse extends BaseBVNResponse{
    private String message;
    private BVNDetail data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BVNDetail getData() {
        return data;
    }

    public void setData(BVNDetail data) {
        this.data = data;
    }
}

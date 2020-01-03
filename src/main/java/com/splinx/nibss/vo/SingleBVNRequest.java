package com.splinx.nibss.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class SingleBVNRequest {


    private String bankVerificationNumber;

    @JsonProperty("BVN")
    public String getBankVerificationNumber() {
        return bankVerificationNumber;
    }

    public void setBankVerificationNumber(String bvn) {
        this.bankVerificationNumber = bvn;
    }
}

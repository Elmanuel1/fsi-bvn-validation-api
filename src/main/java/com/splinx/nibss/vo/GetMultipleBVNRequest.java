package com.splinx.nibss.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMultipleBVNRequest  {
    private List<String> bankVerificationNumbers;

    public List<String> getBankVerificationNumbers() {
        return bankVerificationNumbers;
    }

    public void setBankVerificationNumbers(List<String> bankVerificationNumbers) {
        this.bankVerificationNumbers = bankVerificationNumbers;
    }
}

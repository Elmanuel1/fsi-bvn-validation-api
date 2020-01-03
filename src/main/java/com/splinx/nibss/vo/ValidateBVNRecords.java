package com.splinx.nibss.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateBVNRecords extends BaseBVNResponse {

    private String message;
    private Data data;

    public class Data{
        private List<ValidateBVNDetail> validationResponses;

        public List<ValidateBVNDetail> getValidationResponses() {
            return validationResponses;
        }

        @JsonProperty("ValidationResponses")
        public void setValidationResponses(List<ValidateBVNDetail> validationResponses) {
            this.validationResponses = validationResponses;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

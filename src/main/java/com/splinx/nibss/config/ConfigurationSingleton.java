package com.splinx.nibss.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Service
public class ConfigurationSingleton {

    @Value("${organization.code}")
    private String organizationCode;

    @Value("${sandbox.key}")
    private String sandboxKey;

    @Value("${account.number.length}")
    private Integer accountNumberLength;

    @Value("${bank.code.length}")
    private Integer bankCodeLength;

    @Value("${max.bvn.validation.count}")
    private Integer maxBVNVerifiableCount;

    @Value("${validate.bvn.length}")
    private Boolean validateBVNLength;

    @Value("#{'${bvn.allowed.length}'.split(',')}")
    private List<Integer> allowedBVNLength;



    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SUCCESS_CODE = "00";
    private static final String RESET_HEADER_REQUIRED_ERROR = "01";
    private static final String BAD_REMOTE_RESPONSE_ERROR = "02";
    private static final String CLIENT_ERROR_DURING_RESET_CREDENTIAL_CALL = "03";
    private static final String ARGUMENT_TYPE_NOT_MAPPED_DURING_VALIDATION = "05";
    private static final String ERROR_SETTING_ENCRYPTION_KEYS = "06";
    private static final String ENCRYPTION_ERROR_CODE= "07";
    private static final String DECRYPTION_ERROR_CODE= "08";
    private static final String ENCRYPTION_WITHOUT_IV_ERROR_CODE= "09";
    private static final String EMPTY_KEY_IV_ERROR_CODE= "10";
    private static final String EMPTY_VALUE_ENCRYPT_ERROR_CODE= "11";
    private static final String SERIALIZATION_ERROR_CODE= "12";
    private static final String INVALID_ACCOUNT_NUMBER_BANK_CODE_ERROR_CODE= "13";
    private static final String INVALID_BVN_LENGTH_ERROR_CODE= "14";
    private static final String INVALID_MULTIPLE_BVN_SIZE_ERROR_CODE= "15";
    private static final String REQUEST_MODEL_VALIDATION_ERROR_CODE= "15";

    public  ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public String getResetHeaderRequiredError() {
        return RESET_HEADER_REQUIRED_ERROR;
    }

    public static String getBadRemoteResponseError() {
        return BAD_REMOTE_RESPONSE_ERROR;
    }

    public String getClientErrorDuringResetCredentialCall() {
        return CLIENT_ERROR_DURING_RESET_CREDENTIAL_CALL;
    }


    public String getOrganizationCode() {
        return organizationCode;
    }

    public String getSandboxKey() {
        return sandboxKey;
    }

    public Integer getAccountNumberLength() {
        return accountNumberLength;
    }

    public Integer getBankCodeLength() {
        return bankCodeLength;
    }

    public Integer getMaxBVNVerifiableCount() {
        return maxBVNVerifiableCount;
    }

    public Boolean getValidateBVNLength() {
        return validateBVNLength;
    }

    public List<Integer> getAllowedBVNLength() {
        return allowedBVNLength;
    }

    public String getArgumentTypeNotMappedDuringValidation() {
        return ARGUMENT_TYPE_NOT_MAPPED_DURING_VALIDATION;
    }

    public String getErrorSettingEncryptionKeys() {
        return ERROR_SETTING_ENCRYPTION_KEYS;
    }

    public String getEncryptionErrorCode() {
        return ENCRYPTION_ERROR_CODE;
    }

    public String getDecryptionErrorCode() {
        return DECRYPTION_ERROR_CODE;
    }

    public String getEncryptionWithoutIvErrorCode() {
        return ENCRYPTION_WITHOUT_IV_ERROR_CODE;
    }

    public String getEmptyKeyIvErrorCode() {
        return EMPTY_KEY_IV_ERROR_CODE;
    }

    public String getEmptyValueEncryptErrorCode() {
        return EMPTY_VALUE_ENCRYPT_ERROR_CODE;
    }

    public String getSerializationErrorCode() {
        return SERIALIZATION_ERROR_CODE;
    }

    public String getInvalidAccountNumberBankCodeErrorCode() {
        return INVALID_ACCOUNT_NUMBER_BANK_CODE_ERROR_CODE;
    }

    public String getInvalidBvnLengthErrorCode() {
        return INVALID_BVN_LENGTH_ERROR_CODE;
    }

    public String getInvalidMultipleBvnSizeErrorCode() {
        return INVALID_MULTIPLE_BVN_SIZE_ERROR_CODE;
    }

    public String getRequestModelValidationErrorCode() {
        return REQUEST_MODEL_VALIDATION_ERROR_CODE;
    }

    public static String getSuccessCode() {
        return SUCCESS_CODE;
    }
}

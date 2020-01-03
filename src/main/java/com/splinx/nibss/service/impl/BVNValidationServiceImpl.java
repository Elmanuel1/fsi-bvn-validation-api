package com.splinx.nibss.service.impl;

import com.splinx.nibss.aspect.ValidateBVNLength;
import com.splinx.nibss.config.ConfigurationSingleton;
import com.splinx.nibss.config.SecurityConfiguration;
import com.splinx.nibss.exceptions.*;
import com.splinx.nibss.service.BVNValidationService;

import com.splinx.nibss.util.LoggingUtil;
import com.splinx.nibss.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class BVNValidationServiceImpl implements BVNValidationService {

    public static Logger LOGGER = LoggerFactory.getLogger(BVNValidationServiceImpl.class);

    @Autowired
    private RestService restService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ConfigurationSingleton configurationSingleton;
    @Value("${validate.bvn.length}")
    private Boolean validateBVNLength;

    @Value("#{'${bvn.allowed.length}'.split(',')}")
    private List<Integer> allowedBVNLength;

    @Override
    @ValidateBVNLength(type = SingleBVNRequest.class)
    public VerifySingleBVNResponse verifySingleBVN(SingleBVNRequest request) throws EncryptionException, BadRequestException, BadRemoteResponseException, SerializationException {
        VerifySingleBVNResponse response = null;
        try {

            if (securityConfiguration.getResetConfiguration() == null){
                //you can call the reset password here with the organization code and sandbox key saved in the database
                Map<String, String> headers = new HashMap<>();
                headers.put("sandbox-key", configurationSingleton.getSandboxKey());
                headers.put("organisationcode", configurationSingleton.getOrganizationCode());
                resetCredentials(headers);
            }

            response =  restService.doVerifySingleBVNCall(request);
            response.setMessage("Success");
            response.setResponseCode(configurationSingleton.getSuccessCode());
            return response;


        }catch (Exception e){
            LoggingUtil.log(LOGGER, LoggingUtil.LogLevel.ERROR, "Error occurred during verify single bvn. ", e);
            throw e;
        }
    }

    @Override
    @ValidateBVNLength(type = List.class)
    public VerifyMultipleBVNResponse verifyMultipleBVN(List<String> bankVerificationNumbers) throws EncryptionException, BadRemoteResponseException, BadRequestException, SerializationException {


        if (securityConfiguration.getResetConfiguration() == null){
            //you can call the reset password here with the organization code and sandbox key saved in the database
            Map<String, String> headers = new HashMap<>();
            headers.put("sandbox-key", configurationSingleton.getSandboxKey());
            headers.put("organisationcode", configurationSingleton.getOrganizationCode());
            resetCredentials(headers);
        }

        VerifyMultipleBVNResponse response =  restService.doVerifyMultipleBVNCall(bankVerificationNumbers);
        response.setMessage("Success");
        response.setResponseCode(configurationSingleton.getSuccessCode());
        return response;
    }

    @Override
    @ValidateBVNLength(type = SingleBVNRequest.class)

    public GetSingleBVNResponse getSingleBVN(SingleBVNRequest request) throws EncryptionException, BadRequestException, BadRemoteResponseException, SerializationException {

        try {

            if (securityConfiguration.getResetConfiguration() == null){
                //you can call the reset password here with the organization code and sandbox key saved in the database
                Map<String, String> headers = new HashMap<>();
                headers.put("sandbox-key", configurationSingleton.getSandboxKey());
                headers.put("organisationcode", configurationSingleton.getOrganizationCode());
                resetCredentials(headers);
            }

            GetSingleBVNResponse response =  restService.doGetSingleBVNCall(request);
            response.setMessage("Success");
            response.setResponseCode(configurationSingleton.getSuccessCode());
            return response;
        }catch (Exception e){
            LoggingUtil.log(LOGGER, LoggingUtil.LogLevel.ERROR, "Error occurred during verify single bvn. ", e);
            throw e;
        }

    }

    @Override
    @ValidateBVNLength(type = List.class)
    public GetMultipleBVNResponse getMultipleBVN(List<String> bankVerificationNumbers) throws EncryptionException, BadRemoteResponseException, BadRequestException, SerializationException {


        if (securityConfiguration.getResetConfiguration() == null){
            //you can call the reset password here with the organization code and sandbox key saved in the database
            Map<String, String> headers = new HashMap<>();
            headers.put("sandbox-key", configurationSingleton.getSandboxKey());
            headers.put("organisationcode", configurationSingleton.getOrganizationCode());
            resetCredentials(headers);
        }

        GetMultipleBVNResponse response =  restService.doGetMultipleBVNCall(bankVerificationNumbers);
        response.setMessage("Success");
        response.setResponseCode(configurationSingleton.getSuccessCode());
        return response;

    }

    @Override
    @ValidateBVNLength(type = SingleBVNRequest.class)
    public IsBVNWatchlistedResponse isBVNWatchlisted(SingleBVNRequest request) throws EncryptionException, BadRequestException, BadRemoteResponseException, SerializationException {

        try {

            if (securityConfiguration.getResetConfiguration() == null){
                //you can call the reset password here with the organization code and sandbox key saved in the database
                Map<String, String> headers = new HashMap<>();
                headers.put("sandbox-key", configurationSingleton.getSandboxKey());
                headers.put("organisationcode", configurationSingleton.getOrganizationCode());
                resetCredentials(headers);
            }

            IsBVNWatchlistedResponse response =  restService.doIsBVNWatchlistedCall(request);
            response.setMessage("Success");
            response.setResponseCode(configurationSingleton.getSuccessCode());
            return response;
        }catch (Exception e){
            LoggingUtil.log(LOGGER, LoggingUtil.LogLevel.ERROR, "Error occurred during verify single bvn. ", e);
            throw e;
        }

    }

    @Override
    public ResetConfiguration resetCredentials(Map<String, String> headers) throws BadRequestException, BadRemoteResponseException, EncryptionException {

        ResetConfiguration resetConfiguration  =  restService.doResetCredentialsCall( headers);
        securityConfiguration.setResetConfiguration(resetConfiguration);
        //you can publish to other instance using kafka.
        return resetConfiguration;

    }

    @Override
    @ValidateBVNLength(type = ValidateBVNDetail.class)
    public ValidateBVNRecord validateBVNRecord(ValidateBVNDetail record) throws InvalidBVNException, EncryptionException, BadRequestException, BadRemoteResponseException, SerializationException {
        if (!Objects.equals(configurationSingleton.getAccountNumberLength(), record.getAccountNumber().length())|| !Objects.equals(configurationSingleton.getBankCodeLength(), record.getBankCode().length())){
            throw new InvalidBVNException(configurationSingleton.getInvalidAccountNumberBankCodeErrorCode(), String.format("Invalid account Number or Bankcode passed. Configured length for account number is %s and configured length for bank code is %s", configurationSingleton.getAccountNumberLength().toString(), configurationSingleton.getBankCodeLength().toString()));
        }

        if (securityConfiguration.getResetConfiguration() == null){
            //you can call the reset password here with the organization code and sandbox key saved in the database
            Map<String, String> headers = new HashMap<>();
            headers.put("sandbox-key", configurationSingleton.getSandboxKey());
            headers.put("organisationcode", configurationSingleton.getOrganizationCode());
            resetCredentials(headers);
        }
        ValidateBVNRecord response =  restService.doValidateBVNRecordCall(record);
        response.setMessage("Success");
        response.setResponseCode(configurationSingleton.getSuccessCode());
        return response;

    }

    @Override
    @ValidateBVNLength(type = List.class)
    public ValidateBVNRecords validateBVNRecords(List<ValidateBVNDetail> records) throws EncryptionException, BadRequestException, BadRemoteResponseException, SerializationException {

        if (securityConfiguration.getResetConfiguration() == null){
            //you can call the reset password here with the organization code and sandbox key saved in the database
            Map<String, String> headers = new HashMap<>();
            headers.put("sandbox-key", configurationSingleton.getSandboxKey());
            headers.put("organisationcode", configurationSingleton.getOrganizationCode());
            resetCredentials(headers);
        }

        ValidateBVNRecords response =  restService.doValidateBVNRecordsCall(records);
        response.setMessage("Success");
        response.setResponseCode(configurationSingleton.getSuccessCode());
        return response;
    }
}

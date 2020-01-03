package com.splinx.nibss.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.json.JsonSanitizer;
import com.splinx.nibss.config.ConfigurationSingleton;
import com.splinx.nibss.config.SecurityConfiguration;
import com.splinx.nibss.exceptions.BadRemoteResponseException;
import com.splinx.nibss.exceptions.BadRequestException;
import com.splinx.nibss.exceptions.EncryptionException;
import com.splinx.nibss.exceptions.SerializationException;
import com.splinx.nibss.util.DateFormatter;
import com.splinx.nibss.util.LoggingUtil;
import com.splinx.nibss.util.StringUtils;
import com.splinx.nibss.vo.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class RestService {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ConfigurationSingleton configurationSingleton;


    @Autowired
    private RestTemplate restTemplate;

    @Value("${fsi.sandbox.verify.singlebvn.url}")
    private String verifySingleBVNUrl;

    @Value("${fsi.sandbox.reset.url}")
    private String resetUrl;

    @Value("${fsi.sandbox.get.singlebvn.url}")
    private String getSingleBVNUrl;

    @Value("${fsi.sandbox.is.bvn.watchlisted.url}")
    private String isBVNWatchlistedUrl;

    @Value("${fsi.sandbox.get.multiplebvn.url}")
    private String getMultipleBVNUrl;

    @Value("${fsi.sandbox.verify.multiplebvn.url}")
    private String verifyMultipleBVNUrl;

    @Value("${fsi.sandbox.validate.record.url}")
    private String validateBVNRecordUrl;

    @Value("${fsi.sandbox.validate.records.url}")
    private String validateBVNRecordsUrl;

    public static Logger LOGGER = LoggerFactory.getLogger(BVNValidationServiceImpl.class);

    public IsBVNWatchlistedResponse doIsBVNWatchlistedCall(SingleBVNRequest bvnRequest) throws EncryptionException, BadRequestException, SerializationException, BadRemoteResponseException {

        HttpHeaders headers = generateHttpHeaders();
        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(bvnRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }

        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        IsBVNWatchlistedResponse response;
        try{
            responseEntity =  restTemplate.exchange(isBVNWatchlistedUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(IsBVNWatchlistedResponse) deserializeResponseFromRemote(decrypted, IsBVNWatchlistedResponse.class);
            if (response == null || !Objects.equals("00", response.getData().getResponseCode())){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }

        }catch (HttpClientErrorException e){
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 =(BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);

        }

        return response;
    }

    public GetSingleBVNResponse doGetSingleBVNCall(SingleBVNRequest bvnRequest) throws EncryptionException, BadRequestException, SerializationException {

        HttpHeaders headers = generateHttpHeaders();
        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(bvnRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }

        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        GetSingleBVNResponse response;
        try{
            responseEntity =  restTemplate.exchange(getSingleBVNUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(GetSingleBVNResponse) deserializeResponseFromRemote(decrypted, GetSingleBVNResponse.class);

        }catch (HttpClientErrorException e){
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 =(BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);

        }

        return response;
    }

    public VerifySingleBVNResponse doVerifySingleBVNCall(SingleBVNRequest bvnRequest) throws EncryptionException, BadRequestException, SerializationException, BadRemoteResponseException {

        HttpHeaders headers = generateHttpHeaders();
        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(bvnRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }


        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        VerifySingleBVNResponse response;
        try{
            responseEntity =  restTemplate.exchange(verifySingleBVNUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(VerifySingleBVNResponse) deserializeResponseFromRemote(decrypted, VerifySingleBVNResponse.class);

            if (response == null || !Objects.equals("00", response.getData().getResponseCode())){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }

        }catch (HttpClientErrorException e){
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 =(BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);

        }

        return response;
    }

    public ResetConfiguration doResetCredentialsCall(Map<String, String> headersMap) throws BadRemoteResponseException, BadRequestException {

        HttpHeaders headers = new HttpHeaders();
        String sandboxKey = headersMap.get("sandbox-key");
        String organizationCode = headersMap.get("organisationcode");
        headers.set("OrganisationCode", Base64.encodeBase64String(organizationCode.getBytes()));
        headers.set("Sandbox-Key", sandboxKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity;
        ResetConfiguration securityConfiguration;
        try {
            logHeaders(headers);
            responseEntity = restTemplate.exchange(resetUrl, HttpMethod.POST, entity, String.class);
            headers = responseEntity.getHeaders();
            logHeaders(headers);

            securityConfiguration = new ResetConfiguration();
            if (!(headers.containsKey("Code") && headers.containsKey("AES_KEY") && headers.containsKey("IVKey") && headers.containsKey("PASSWORD"))){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }

            securityConfiguration.setCode(headers.get("Code").get(0));
            securityConfiguration.setAesKey(headers.get("AES_KEY").get(0));
            securityConfiguration.setIvKey(headers.get("IVKey").get(0));
            securityConfiguration.setPassword(headers.get("PASSWORD").get(0));
            securityConfiguration.setSandboxKey(sandboxKey);

        }catch (HttpClientErrorException  ex){
            headers = ex.getResponseHeaders();
            logHeaders(headers);
            throw new BadRequestException(configurationSingleton.getClientErrorDuringResetCredentialCall(), "Client error caused reset credential failure", ex);
        }
        return securityConfiguration;
    }

    public GetMultipleBVNResponse doGetMultipleBVNCall(List<String> bankVerificationNumbers) throws EncryptionException, BadRequestException, SerializationException, BadRemoteResponseException {

        HttpHeaders headers = generateHttpHeaders();

        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);

        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        GetMultipleBVNResponse response;
        try{
            responseEntity =  restTemplate.exchange(getMultipleBVNUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(GetMultipleBVNResponse) deserializeResponseFromRemote(decrypted, GetMultipleBVNResponse.class);

            if (response == null || !Objects.equals("00", response.getData().getResponseCode())){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }

        }catch (HttpClientErrorException e) {
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 = (BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);
        }
        return response;
    }



    public VerifyMultipleBVNResponse doVerifyMultipleBVNCall(List<String> bankVerificationNumbers) throws EncryptionException, BadRequestException, SerializationException, BadRemoteResponseException {

        HttpHeaders headers = generateHttpHeaders();

        MultipleBVNRequest request = new MultipleBVNRequest();

        String result = bankVerificationNumbers
                .stream()
                .reduce("", (partialString, element) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(partialString);
                    if (!StringUtils.isBlank(partialString))
                        stringBuilder.append(", ");

                    stringBuilder.append(element);
                    return stringBuilder.toString();
                });
        request.setBankVerificationNumbers(result);

        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }
        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        VerifyMultipleBVNResponse response;
        try{
            responseEntity =  restTemplate.exchange(verifyMultipleBVNUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(VerifyMultipleBVNResponse) deserializeResponseFromRemote(decrypted, VerifyMultipleBVNResponse.class);

            if (response == null || !Objects.equals("00", response.getData().getResponseCode())){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }


        }catch (HttpClientErrorException e) {
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 = (BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);
        }
        return response;
    }



    public ValidateBVNRecord doValidateBVNRecordCall(ValidateBVNDetail bvnRequest) throws EncryptionException, BadRequestException, SerializationException, BadRemoteResponseException {

        HttpHeaders headers = generateHttpHeaders();
        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(bvnRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }

        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        ValidateBVNRecord response;
        try{
            responseEntity =  restTemplate.exchange(validateBVNRecordUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(ValidateBVNRecord) deserializeResponseFromRemote(decrypted, ValidateBVNRecord.class);

            if (response == null || !Objects.equals("00", response.getData().getResponseCode())){
                throw new BadRemoteResponseException(ConfigurationSingleton.getBadRemoteResponseError(), "Remote responded with incomplete data");
            }

        }catch (HttpClientErrorException e){
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 =(BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);

        }

        return response;
    }


    public ValidateBVNRecords doValidateBVNRecordsCall(List<ValidateBVNDetail> bvnRequest) throws EncryptionException, BadRequestException, SerializationException {

        HttpHeaders headers = generateHttpHeaders();
        String bvnRequestString = null;
        try {
            bvnRequestString = configurationSingleton.getObjectMapper().writeValueAsString(bvnRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }

        String encryptedHex = securityConfiguration.encrypt(bvnRequestString);
        logHeaders(headers);
        logBody(bvnRequestString);
        logBody(encryptedHex);

        HttpEntity<String> entity = new HttpEntity<>(encryptedHex, headers);
        ResponseEntity<String> responseEntity;
        String responseBody = null;
        ValidateBVNRecords response;
        try{
            responseEntity =  restTemplate.exchange(validateBVNRecordsUrl, HttpMethod.POST, entity, String.class);

            if (responseEntity != null){
                responseBody = responseEntity.getBody();
                headers = responseEntity.getHeaders();
            }
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            logHeaders(headers);
            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            response =(ValidateBVNRecords) deserializeResponseFromRemote(decrypted, ValidateBVNRecords.class);


        }catch (HttpClientErrorException e){
            responseBody = e.getResponseBodyAsString();
            headers = e.getResponseHeaders();
            logHeaders(headers);
            if (!StringUtils.isBlank(responseBody))
                logBody(responseBody);

            String decrypted = securityConfiguration.decrypt(responseBody);
            logBody(decrypted);
            BVNResponse response2 =(BVNResponse) deserializeResponseFromRemote(decrypted, BVNResponse.class);
            throw new BadRequestException(response2.getResponseCode(), response2.getMessage(), e);

        }

        return response;
    }


    private HttpHeaders generateHttpHeaders() throws EncryptionException {

        final String sandBoxKey = securityConfiguration.getResetConfiguration().getSandboxKey();
        final String signatureMethodHeader = "SHA256";
        final String password = securityConfiguration.getResetConfiguration().getPassword();
        final String username = securityConfiguration.getResetConfiguration().getCode();
        final String requestDate = DateFormatter.formatDate(new Date());
        // Concatenate all three strings into one string
        final String signatureString = username + requestDate + password;

        // Concatenate the strings in the format username:password
        final String authString = username + ':' + password;

        // Encode it to Base64 and save it for it will be used later
        final String authHeader = Base64.encodeBase64String(authString.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authHeader);


        byte[] sigByte = securityConfiguration.getSHA(signatureString);
        httpHeaders.set("SIGNATURE", StringUtils.toHexString(sigByte));
        httpHeaders.set("OrganisationCode", Base64.encodeBase64String(username.getBytes()));
        httpHeaders.set("SIGNATURE_METH", signatureMethodHeader);
        httpHeaders.set("Sandbox-Key", sandBoxKey);

        return httpHeaders;
        
    }

    private void logHeaders(HttpHeaders headers) {
        try {
            StringBuffer buffer = new StringBuffer();
            Map<String, String> head = headers.toSingleValueMap();
            for (Map.Entry<String, String> heade : head.entrySet()) {
//                if (Objects.equals("Authorization", heade.getValue()) || Objects.equals("access_token", heade.getValue())) {
//                    continue;
//                }
                buffer.append(heade.getKey());
                buffer.append(" = ");
                buffer.append(heade.getValue());
            }
            LoggingUtil.log(LOGGER, LoggingUtil.LogLevel.ERROR, "Headers: " + configurationSingleton.getObjectMapper().writeValueAsString(head.toString()));
        } catch (Exception e) {
            LOGGER.error("Could not log headers", e);
        }
    }

    private void logBody(String body) {
        LoggingUtil.log(LOGGER, LoggingUtil.LogLevel.INFO, "Body : " + body);
    }

    private Object deserializeResponseFromRemote(String response, Class remoteClass) throws SerializationException {

        response = JsonSanitizer.sanitize(response);
        Object obj = null;
        try {
            obj = configurationSingleton.getObjectMapper().readValue(response, remoteClass);
        } catch (IOException e) {
            throw new SerializationException(configurationSingleton.getSerializationErrorCode(), e.getMessage(), e);
        }
        return obj;
    }


}

package com.splinx.nibss.service;

import com.splinx.nibss.exceptions.*;
import com.splinx.nibss.vo.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * This interface defines all the contracts available on the Sandbox portal
 */
public interface BVNValidationService {
    VerifySingleBVNResponse verifySingleBVN(SingleBVNRequest singleBVNRequest) throws InvalidBVNException, EncryptionException, BadRequestException, IOException, InvalidResetConfigurationException, BadRemoteResponseException, SerializationException;
    VerifyMultipleBVNResponse verifyMultipleBVN(List<String> bankVerificationNumbers) throws InvalidBVNException, EncryptionException, BadRemoteResponseException, BadRequestException, IOException, SerializationException;
    GetSingleBVNResponse getSingleBVN(SingleBVNRequest request) throws EncryptionException, InvalidBVNException, IOException, BadRequestException, BadRemoteResponseException, SerializationException;
    GetMultipleBVNResponse getMultipleBVN(List<String> bankVerficationNumbers) throws InvalidBVNException, EncryptionException, BadRemoteResponseException, BadRequestException, IOException, SerializationException;
    IsBVNWatchlistedResponse isBVNWatchlisted(SingleBVNRequest request) throws EncryptionException, InvalidBVNException, IOException, BadRequestException, BadRemoteResponseException, SerializationException;
    ResetConfiguration resetCredentials(Map<String, String> headers) throws BadRequestException, BadRemoteResponseException, EncryptionException;
    ValidateBVNRecord validateBVNRecord(ValidateBVNDetail record) throws InvalidBVNException, EncryptionException, BadRequestException, IOException, BadRemoteResponseException, SerializationException;
    ValidateBVNRecords validateBVNRecords(List<ValidateBVNDetail> record) throws InvalidBVNException, EncryptionException, BadRequestException, IOException, BadRemoteResponseException, SerializationException;
}

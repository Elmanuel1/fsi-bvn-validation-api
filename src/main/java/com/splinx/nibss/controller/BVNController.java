package com.splinx.nibss.controller;


import com.splinx.nibss.aspect.ValidateHeaders;
import com.splinx.nibss.exceptions.*;

import com.splinx.nibss.service.BVNValidationService;
import com.splinx.nibss.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
public class BVNController {

    @Autowired
    private BVNValidationService bvnValidationService;

    @RequestMapping(value="/Reset", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ValidateHeaders
    @ResponseBody
    public ResetConfiguration resetCredentials(@RequestHeader Map<String, String> headers) throws EncryptionException, BadRemoteResponseException, BadRequestException {
        return bvnValidationService.resetCredentials(headers);
    }


    @RequestMapping(value="/VerifySingleBVN", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public VerifySingleBVNResponse verifySingleBVN(@RequestBody @Valid SingleBVNRequest bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, InvalidResetConfigurationException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.verifySingleBVN(bvnRequest);
    }

    @RequestMapping(value="/GetSingleBVN", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public GetSingleBVNResponse getSingleBVN(@RequestBody @Valid SingleBVNRequest bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.getSingleBVN(bvnRequest);
    }

    @RequestMapping(value="/IsBVNWatchlisted", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public IsBVNWatchlistedResponse IsBVNWatchlisted(@RequestBody @Valid SingleBVNRequest bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.isBVNWatchlisted(bvnRequest);
    }

    @RequestMapping(value="/GetMultipleBVN", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public GetMultipleBVNResponse getMultipleBVN(@RequestBody @Valid GetMultipleBVNRequest bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.getMultipleBVN(bvnRequest.getBankVerificationNumbers());
    }

    @RequestMapping(value="/VerifyMultipleBVN", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public VerifyMultipleBVNResponse verifyMultipleBVN(@RequestBody @Valid GetMultipleBVNRequest bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.verifyMultipleBVN(bvnRequest.getBankVerificationNumbers());
    }

    @RequestMapping(value="/ValidateRecord", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public ValidateBVNRecord validateBVNRecord(@RequestBody @Valid ValidateBVNDetail bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.validateBVNRecord(bvnRequest);
    }

    @RequestMapping(value="/ValidateRecords", method=RequestMethod.POST, consumes= MediaType.ALL_VALUE, produces=MediaType.ALL_VALUE)
    @ResponseBody
    public ValidateBVNRecords validateBVNRecords(@RequestBody @Valid List<ValidateBVNDetail> bvnRequest) throws EncryptionException, InvalidBVNException, BadRequestException, IOException, BadRemoteResponseException, SerializationException {
        return bvnValidationService.validateBVNRecords(bvnRequest);
    }




}
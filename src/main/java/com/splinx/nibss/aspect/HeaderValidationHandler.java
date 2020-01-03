package com.splinx.nibss.aspect;

import com.splinx.nibss.config.ConfigurationSingleton;
import com.splinx.nibss.exceptions.ArgumentTypeNotMappedException;
import com.splinx.nibss.exceptions.BadRequestException;
import com.splinx.nibss.exceptions.InvalidBVNException;
import com.splinx.nibss.util.BVNUtilities;
import com.splinx.nibss.util.StringUtils;
import com.splinx.nibss.vo.SingleBVNRequest;
import com.splinx.nibss.vo.ValidateBVNDetail;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Aspect
@Component
public class HeaderValidationHandler {

    @Autowired
    private ConfigurationSingleton configurationSingleton;
    public static Logger LOGGER = LoggerFactory.getLogger(HeaderValidationHandler.class);

    @Before("@annotation(com.splinx.nibss.aspect.ValidateHeaders)")
    public void validateHeaders(JoinPoint joinPoint) throws BadRequestException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        String organisationCode = request.getHeader("OrganisationCode");
        String sandBoxKey = request.getHeader("Sandbox-Key");

        if (StringUtils.isBlank(organisationCode) || StringUtils.isBlank(sandBoxKey)){
            throw new BadRequestException(configurationSingleton.getResetHeaderRequiredError(), "OrganisationCode or Sandbox-Key values is missing in the request headers");
        }
    }

    @Before("@annotation(com.splinx.nibss.aspect.ValidateBVNLength)")
    public void validateBVNLength(JoinPoint joinPoint) throws InvalidBVNException, ArgumentTypeNotMappedException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(ValidateBVNLength.class)) {
            final ValidateBVNLength annotation = method.getAnnotation(ValidateBVNLength.class);
            final Class type = annotation.type();
            if (Objects.equals(SingleBVNRequest.class, type)){
                SingleBVNRequest request = (SingleBVNRequest) joinPoint.getArgs()[0];

                if (configurationSingleton.getValidateBVNLength() != null && configurationSingleton.getValidateBVNLength() && !BVNUtilities.lengthIsExpected(request.getBankVerificationNumber(), configurationSingleton.getAllowedBVNLength())){
                    throw new InvalidBVNException(configurationSingleton.getInvalidBvnLengthErrorCode(), "BVN passed is less or more than the configured length for BVN");
                }
            }

            if (Objects.equals(ValidateBVNDetail.class, type)){
                ValidateBVNDetail request = (ValidateBVNDetail) joinPoint.getArgs()[0];
                if (configurationSingleton.getValidateBVNLength() != null && configurationSingleton.getValidateBVNLength() && !BVNUtilities.lengthIsExpected(request.getBvn(), configurationSingleton.getAllowedBVNLength())){
                    throw new InvalidBVNException(configurationSingleton.getInvalidBvnLengthErrorCode(), "BVN passed is more than the configured length for BVN");
                }

            }

            if (Objects.equals(List.class, type)){
                List<?> request = (List<?>)  joinPoint.getArgs()[0];

                if (request == null){
                    throw new InvalidBVNException(configurationSingleton.getInvalidMultipleBvnSizeErrorCode(), String.format("Please review the number of BVN validated. Configured size is %s", configurationSingleton.getMaxBVNVerifiableCount().toString()));
                }

                if (request != null && request.size() > configurationSingleton.getMaxBVNVerifiableCount()){
                    throw new InvalidBVNException(configurationSingleton.getInvalidMultipleBvnSizeErrorCode(), String.format("Please review the number of BVN validated. Configured size is %s", configurationSingleton.getMaxBVNVerifiableCount().toString()));
                }

                List<String> list  = null;
                if(!request.isEmpty() && request.get(0) instanceof String){
                    list  = (List<String>) request;

                }
                else if(!request.isEmpty() && request.get(0) instanceof ValidateBVNDetail){
                    List<ValidateBVNDetail> bvnList  = (List<ValidateBVNDetail>) request;

                    list = bvnList.stream()
                            .map(record-> record.getBvn())
                            .collect(Collectors.toList());
                }

                if (list == null){
                    throw new ArgumentTypeNotMappedException(configurationSingleton.getArgumentTypeNotMappedDuringValidation(), "No BVN found to be validated" );
                }
                if (configurationSingleton.getValidateBVNLength() != null && configurationSingleton.getValidateBVNLength() && !BVNUtilities.lengthIsExpected(list, configurationSingleton.getAllowedBVNLength())){
                    throw new InvalidBVNException(configurationSingleton.getInvalidBvnLengthErrorCode(), "One or more of the BVN passed is less or more than the configured length for BVN allowed");
                }


            }
        }

    }
}
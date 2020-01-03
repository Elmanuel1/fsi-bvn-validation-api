package com.splinx.nibss.advice;


import com.splinx.nibss.config.ConfigurationSingleton;
import com.splinx.nibss.exceptions.*;
import com.splinx.nibss.vo.BVNResponse;
import com.splinx.nibss.vo.BaseBVNResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private ConfigurationSingleton configurationSingleton;

    @ExceptionHandler({ArgumentTypeNotMappedException.class, BadRemoteResponseException.class,
            BadRequestException.class, EncryptionException.class, InvalidBVNException.class,
            SerializationException.class, ConstraintViolationException.class
    })
    public ResponseEntity<Object> handleError(HttpServletRequest req, Exception ex) {

        BaseBVNResponse response = new BaseBVNResponse();
        if (ex.getCause() instanceof EncryptionException || ex instanceof EncryptionException){
            if (ex.getCause() instanceof EncryptionException){
                ex = (Exception) ex.getCause();
            }
            EncryptionException e = (EncryptionException) ex;
            response.setMessage("Error occurred during processing. Please contact admin for resolution.");
            response.setResponseCode(e.getCode());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else   if (ex.getCause() instanceof BadRequestException  || ex instanceof BadRequestException){
            if (ex.getCause() instanceof BadRequestException){
                ex = (Exception) ex.getCause();
            }

            BadRequestException e = (BadRequestException) ex;
            response.setMessage(e.getMessage());
            response.setResponseCode(e.getCode());
        }

        else if (ex.getCause() instanceof SerializationException  || ex instanceof SerializationException){
            SerializationException e = (SerializationException) ex;
            response.setMessage("Error occurred during processing. Please try again later.");
            response.setResponseCode(e.getCode());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

        else if (ex.getCause() instanceof BadRemoteResponseException  || ex instanceof BadRemoteResponseException){
            BadRemoteResponseException e = (BadRemoteResponseException) ex;
            response.setMessage("Error occurred during processing. Please try again later.");
            response.setResponseCode(e.getCode());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        else if (ex.getCause() instanceof InvalidBVNException  || ex instanceof InvalidBVNException){

            if (ex.getCause() instanceof InvalidBVNException){
                ex = (Exception) ex.getCause();
            }

            InvalidBVNException e = (InvalidBVNException) ex;
            response.setMessage(e.getMessage());
            response.setResponseCode(e.getCode());
        }else if (ex.getCause() instanceof ArgumentTypeNotMappedException  || ex instanceof ArgumentTypeNotMappedException){

            if (ex.getCause() instanceof ArgumentTypeNotMappedException){
                ex = (Exception) ex.getCause();
            }
            ArgumentTypeNotMappedException e = (ArgumentTypeNotMappedException) ex;
            response.setMessage(e.getMessage());
            response.setResponseCode(e.getCode());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }else if(ex.getCause() instanceof ConstraintViolationException  || ex instanceof ConstraintViolationException){

            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) ex).getConstraintViolations();

            ConstraintViolation constraintViolation = constraintViolations
                    .stream()
                    .findFirst()
                    .get();
            String message = String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
                            constraintViolation.getInvalidValue(), constraintViolation.getMessage());

            response.setMessage(message);
            response.setResponseCode(configurationSingleton.getRequestModelValidationErrorCode());
        }

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

        BVNResponse response = new BVNResponse();
        response.setResponseCode(configurationSingleton.getRequestModelValidationErrorCode());
        if (!errorList.isEmpty()){
            response.setMessage(errorList.get(0));
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BVNResponse response = new BVNResponse();
        response.setResponseCode(configurationSingleton.getRequestModelValidationErrorCode());
        response.setMessage("Invalid Request body. Please review...");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

}

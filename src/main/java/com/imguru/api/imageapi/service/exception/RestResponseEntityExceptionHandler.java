package com.imguru.api.imageapi.service.exception;

import com.imguru.api.imageapi.service.model.RestError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        if (ex instanceof HttpClientErrorException.NotFound) {
            RestError restError = new RestError(HttpStatus.NOT_FOUND.toString(), "Resource Not Found with Request Id");
            return new ResponseEntity<Object>(
                    restError, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } else if (ex instanceof HttpServerErrorException.InternalServerError) {
            RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Something went wrong");
            return new ResponseEntity<Object>(
                    restError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
        } else {
            RestError restError = new RestError(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
            return new ResponseEntity<Object>(
                    restError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

}
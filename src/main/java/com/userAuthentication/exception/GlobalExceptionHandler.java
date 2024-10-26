package com.userAuthentication.exception;

import com.userAuthentication.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SignatureException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CmsResponseStatusException.class, SignatureException.class})

    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCmsResponseStatusException(CmsResponseStatusException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getSource(),
                ex.getErrorCode(),
                ex.getMessage()
        );

        // Return the response entity with the HTTP status from the exception
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<String> handleJwtTokenException(JwtTokenException ex) {
        // Log the exception if needed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}

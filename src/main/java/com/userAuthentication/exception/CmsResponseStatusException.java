package com.userAuthentication.exception;

import org.springframework.http.HttpStatus;

public class CmsResponseStatusException extends RuntimeException {
    private static final long serialVersionUID = 8827962559959765527L;
    private final HttpStatus httpStatus;
    private final String source;
    private final String errorCode;

    public CmsResponseStatusException(final HttpStatus httpStatus, final String source, final String errorCode) {
        super(httpStatus.toString());
        this.httpStatus = httpStatus;
        this.source = source;
        this.errorCode = errorCode;
    }
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getSource() {
        return this.source;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
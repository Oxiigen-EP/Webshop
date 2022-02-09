package com.wsp.webshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class RestBase<T> extends ResponseEntity<T> {


    private String responseMessage;

    public RestBase(HttpStatus status, String responseMessage) {
        super(status);
        this.responseMessage = responseMessage;
    }

    public RestBase(T body, HttpStatus status, String responseMessage) {
        super(body, status);
        this.responseMessage = responseMessage;
    }

    public RestBase(MultiValueMap<String, String> headers, HttpStatus status, String responseMessage) {
        super(headers, status);
        this.responseMessage = responseMessage;
    }

    public RestBase(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public RestBase(T body, MultiValueMap<String, String> headers, HttpStatus status, String responseMessage) {
        super(body, headers, status);
        this.responseMessage = responseMessage;
    }

    public RestBase(T body, MultiValueMap<String, String> headers, int rawStatus, String responseMessage) {
        super(body, headers, rawStatus);
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}

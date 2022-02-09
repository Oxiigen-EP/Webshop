package com.wsp.webshop.response;

import com.wsp.webshop.api.controller.RestBase;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static RestBase<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        return new RestBase<Object>(map,status,null);
    }
}

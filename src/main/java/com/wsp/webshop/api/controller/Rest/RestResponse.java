package com.wsp.webshop.api.controller.Rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestResponse {
    private String result = "Success";
    private String errorCode;
    private String errorMessage;
}
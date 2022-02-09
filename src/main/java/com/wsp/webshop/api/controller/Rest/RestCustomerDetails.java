package com.wsp.webshop.api.controller.Rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RestCustomerDetails extends RestResponse {
    private String customer_id;
    private String first_name;
    private String last_name;
    private String email;

}

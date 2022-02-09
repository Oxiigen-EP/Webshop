package com.wsp.webshop.api.controller;

import com.wsp.webshop.model.OrderItem;
import com.wsp.webshop.model.WebshopOrder;
import com.wsp.webshop.repository.OrderRepository;

import com.wsp.webshop.repository.ProductRepository;
import com.wsp.webshop.response.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/app-api/orders")
public class WebshopOrderApiController {

   @Autowired
   OrderRepository orderRepo;
   @Autowired
   ProductRepository prodRepo;



    @GetMapping
    public Iterable<WebshopOrder> getOrders() throws ParseException {

        System.out.println(getTecajEur());
        return orderRepo.findAll();
    }


    @PostMapping(consumes= "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public WebshopOrder create(@RequestBody WebshopOrder webshopOrder){return orderRepo.save(webshopOrder);}


    @PatchMapping(path="/fin/{id}", consumes= "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> finishOrder(@PathVariable("id") long id,
                              @Valid @RequestBody WebshopOrder patchOrder){

        BigDecimal totalPrice=BigDecimal.ZERO;

       try {
           WebshopOrder webshopOrder = orderRepo.findById(id).get();
           List<OrderItem> orderItems = webshopOrder.getOrderItem();

           for(OrderItem oi : orderItems){
               totalPrice =totalPrice.add(oi.getProduct().getPrice_hrk().multiply(BigDecimal.valueOf(oi.getQuantity())))  ;
           }

           webshopOrder.setTotal_price_hrk(totalPrice.setScale(2, RoundingMode.HALF_UP));
           webshopOrder.setTotal_price_eur(totalPrice.multiply(getTecajEur()).setScale(2, RoundingMode.HALF_UP));
           webshopOrder.setOrder_status("FINISHED");

           System.out.println(webshopOrder.getTotal_price_hrk());
           System.out.println(getTecajEur());
           System.out.println(webshopOrder.getTotal_price_eur());
           System.out.println(webshopOrder.getOrder_status());

           //orderRepo.save(webshopOrder);
           return ResponseHandler.generateResponse("Uspješna promjena", HttpStatus.CREATED, webshopOrder);
       } catch (Exception e1) {
           System.out.println(e1.getMessage());
           return ResponseHandler.generateResponse(e1.getMessage(), HttpStatus.MULTI_STATUS, null);
       }

    }


    public BigDecimal getTotalPrice(long id){
        BigDecimal sumPrice = BigDecimal.ZERO;
       // List<Product> orderProducts =

        return sumPrice;
    }

    //Dohvat tečaja i konverzija
    public List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject)jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }

    public BigDecimal getTecajEur() throws ParseException {
        RestTemplate restTemplate = new RestTemplate(); //1
        String url = "https://api.hnb.hr/tecajn/v1?valuta=EUR"; //2
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class); //3

        List<String> tecaj_eur = getValuesForGivenKey(response.getBody(), "Srednji za devize");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        // parse the string value
        BigDecimal parsedStringValue = (BigDecimal) decimalFormat.parse(tecaj_eur.get(0));

        return parsedStringValue;
    }


}

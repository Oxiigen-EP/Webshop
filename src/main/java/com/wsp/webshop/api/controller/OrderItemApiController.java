package com.wsp.webshop.api.controller;


import com.wsp.webshop.exception.ResourceNotFoundException;
import com.wsp.webshop.model.OrderItem;
import com.wsp.webshop.model.Product;
import com.wsp.webshop.model.WebshopOrder;
import com.wsp.webshop.repository.OrderItemRepository;
import com.wsp.webshop.repository.OrderRepository;
import com.wsp.webshop.repository.ProductRepository;

import com.wsp.webshop.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/app-api/orderitems")
public class OrderItemApiController {

    @Autowired
    OrderItemRepository orderitemRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    ProductRepository prodRepo;

    @GetMapping
    public Iterable<OrderItem> getOrderItems() throws ParseException {
        return orderitemRepo.findAll();
    }

    @PostMapping(consumes= "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public RestBase<Object> create(@RequestBody OrderItem orderItem) {
        try {
            validateProductsAvaliable(orderItem.getProduct().getProduct_id());
            OrderItem oItem = orderitemRepo.save(orderItem);
            return ResponseHandler.generateResponse("Uspješno dodano",HttpStatus.CREATED,oItem);
        } catch (ResponseStatusException e){
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.MULTI_STATUS,null);
        }
    }


    @PatchMapping(path="/{id}", consumes= "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> partialUpdateProd(@PathVariable("id") long id,
                                              @Valid @RequestBody OrderItem patchOrderItem) {
        OrderItem oItem = orderitemRepo.findById(id).get();
        Product product = prodRepo.findById(patchOrderItem.getProduct().getProduct_id()).get();
        // WebshopOrder webshopOrder = orderRepo.findById(order_id).get();

     try {
         validateProductsAvaliable(product.getProduct_id());

         if (patchOrderItem.getQuantity() > 0) {
             oItem.setQuantity(patchOrderItem.getQuantity());
         }

         if (patchOrderItem.getProduct() != null) {
             if (product.getIs_avaliable()) {
                 oItem.setProduct(product);
                 orderitemRepo.save(oItem);
             } else {
                 System.out.println("Proizvod n1je dostupan");
             }
         }

         String uri = ServletUriComponentsBuilder
                 .fromCurrentServletMapping()
                 .path("/orderitems/{id}")
                 .buildAndExpand(id)
                 .toString();

         HttpHeaders headers = new HttpHeaders();
         headers.add("Location", uri);

         return ResponseHandler.generateResponse("Uspješna promjena",HttpStatus.CREATED,oItem);
     } catch (ResponseStatusException e){
         System.out.println(e.getMessage());
        return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.MULTI_STATUS,null);
    } catch (Exception e1) {
         System.out.println(e1.getMessage());
         return ResponseHandler.generateResponse(e1.getMessage(), HttpStatus.MULTI_STATUS, null);
     }
}


    private WebshopOrder getWebOrder(Long id){
        WebshopOrder webshopOrder = orderRepo.findById(id).get();
        return orderRepo.save(webshopOrder);
    }

    private void validateProductsAvaliable(long id) {

        Product product = prodRepo.findById(id).get();
        if(!product.getIs_avaliable()){

            ResourceNotFoundException exc = new ResourceNotFoundException("Proizvod trenutno nije dostupan!");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nije dostupno", exc);
        }
    }

}

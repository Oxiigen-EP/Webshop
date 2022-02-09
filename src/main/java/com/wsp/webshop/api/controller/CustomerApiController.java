package com.wsp.webshop.api.controller;

import com.wsp.webshop.model.Customer;
import com.wsp.webshop.repository.CustomerRepository;
import com.wsp.webshop.response.ResponseHandler;
import com.wsp.webshop.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app-api/customers")
public class CustomerApiController {

    @Autowired
    CustomerRepository custRepo;

    @Autowired
    CustomerServiceImpl custService;

    @GetMapping
    public List<Customer> getCustomer(){
        return custService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable("id") Long id){
        return custService.getByID(id);
    }

    @PostMapping(consumes= "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public RestBase<Object> create(@RequestBody Customer customer) {
        try {
            Customer cust = custService.saveNew(customer);
            return ResponseHandler.generateResponse("Uspješno dodano",HttpStatus.CREATED,cust);
        } catch (ResponseStatusException e){
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.MULTI_STATUS,null);
        }
    }

    @PatchMapping(path="/{id}", consumes= "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> partialUpdate(@PathVariable("id") long id, @Valid @RequestBody Customer patchCustomer) {
        try {
            Optional<Customer> cust = custService.getByID(id);
            if(cust.isPresent()) {
                if (patchCustomer.getFirst_name() != null) {
                    cust.get().setFirst_name(patchCustomer.getFirst_name());
                }
                if (patchCustomer.getLast_name() != null) {
                    cust.get().setLast_name(patchCustomer.getLast_name());
                }
                if (patchCustomer.getEmail() != null) {
                    cust.get().setEmail(patchCustomer.getEmail());
                }
                custService.saveNew(cust.get());
                return ResponseHandler.generateResponse("Uspješna promjena", HttpStatus.OK, cust);
            }else{
                return ResponseHandler.generateResponse("NE postoji klijent za update", HttpStatus.MULTI_STATUS, null);
            }
        } catch (ResponseStatusException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> delete(@PathVariable("id") Long id) {
        try {
            Optional<Customer> cust = custService.getByID(id);
            if(cust.isPresent()) {
                custService.delete(cust.get());
                return ResponseHandler.generateResponse("Uspješno obrisano", HttpStatus.NO_CONTENT, cust);
            } else {
                return ResponseHandler.generateResponse("NE postoji klijent", HttpStatus.MULTI_STATUS, null);
            }
        }catch(EmptyResultDataAccessException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }
}




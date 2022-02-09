package com.wsp.webshop.api.controller;

import com.wsp.webshop.model.Customer;
import com.wsp.webshop.model.Product;
import com.wsp.webshop.repository.ProductRepository;

import com.wsp.webshop.response.ResponseHandler;
import com.wsp.webshop.service.ProductServiceImpl;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app-api/products")
public class ProductApiController {


    @Autowired
    ProductServiceImpl prodService;

    @Autowired
    ProductRepository prodRepo;

    @GetMapping
    public List<Product> getProducts(){
        return prodService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProjectById(@PathVariable("id") Long id){
        return prodService.getByID(id);
    }


    @PostMapping(consumes= "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public RestBase<Object> create(@RequestBody Product product) {
        try {
            Product prod = prodService.saveNew(product);
            return ResponseHandler.generateResponse("Uspješno dodano",HttpStatus.CREATED,prod);
        } catch (ResponseStatusException e){
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.MULTI_STATUS,null);
        } catch (DataIntegrityViolationException ex) {
            return ResponseHandler.generateResponse("Proizvod s šifrom "+product.getCode()+" već postoji" ,HttpStatus.MULTI_STATUS,null);
        }
    }


    @PatchMapping(path="/{id}", consumes= "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> partialUpdate(@PathVariable("id") long id, @Valid @RequestBody Product patchProduct) {

       try {
           Optional<Product> pro = prodService.getByID(id);

        if (pro.isPresent()) {
            if (patchProduct.getCode() != null) {
                pro.get().setCode(patchProduct.getCode());
            }
            if (patchProduct.getProduct_name() != null) {
                pro.get().setProduct_name(patchProduct.getProduct_name());
            }
            if (patchProduct.getPrice_hrk() != null) {
                pro.get().setPrice_hrk(patchProduct.getPrice_hrk());
            }
            if (patchProduct.getDescription() != null) {
                pro.get().setDescription(patchProduct.getDescription());
            }
            if (patchProduct.getIs_avaliable() != null) {
                pro.get().setIs_avaliable(patchProduct.getIs_avaliable());
            }
            prodService.saveNew(pro.get());

            return ResponseHandler.generateResponse("Uspješna promjena", HttpStatus.CREATED, pro);
        } else{
            return ResponseHandler.generateResponse("NE postoji proizvod za update", HttpStatus.MULTI_STATUS, null);
        }
       }catch (ResponseStatusException e) {
           return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
       }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus
    @ResponseBody
    public RestBase<Object> delete(@PathVariable("id") Long id) {
        try {
            Optional<Product> prod = prodService.getByID(id);
            if(prod.isPresent()) {
                prodService.delete(prod.get());
                return ResponseHandler.generateResponse("Uspješno obrisano", HttpStatus.NO_CONTENT, prod);
            } else {
                return ResponseHandler.generateResponse("NE postoji artikl", HttpStatus.MULTI_STATUS, null);
            }
        }catch(EmptyResultDataAccessException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

}

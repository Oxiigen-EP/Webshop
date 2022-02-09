package com.wsp.webshop.service;

import com.wsp.webshop.exception.ApiException;
import com.wsp.webshop.model.Customer;
import com.wsp.webshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements TempService<Customer, Long> {

    @Autowired
    CustomerRepository custRepo;

    @Override
    public List<Customer> getAll() {
        return custRepo.findAll();
    }

    @Override
    public Optional<Customer> getByID(Long id) {

           Optional<Customer> cust = custRepo.findById(id);
           return cust;
    }

    @Override
    public Customer saveNew(Customer customer) {
        return custRepo.save(customer);
    }

    @Override
    public void delete(Customer customer) {
     custRepo.delete(customer);
    }



}

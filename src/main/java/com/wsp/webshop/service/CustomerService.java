package com.wsp.webshop.service;

import com.wsp.webshop.model.Customer;

import java.util.List;

public interface CustomerService {

 public List<Customer> getAllCustomers();

 public Customer getCustomer(long id);

 public Customer saveNewCustomer(Customer customer);

 public void deleteCustomer(Customer customer);

}

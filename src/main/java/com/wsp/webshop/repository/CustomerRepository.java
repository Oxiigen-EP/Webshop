package com.wsp.webshop.repository;

import com.wsp.webshop.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "apicustomers", path="apicustomers")
public interface CustomerRepository extends CrudRepository<Customer,Long> {

    @Override
    public List<Customer> findAll();

}

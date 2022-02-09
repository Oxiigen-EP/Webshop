package com.wsp.webshop.repository;


import com.wsp.webshop.model.Customer;
import com.wsp.webshop.model.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "apiproducts", path="apiproducts")
public interface ProductRepository extends CrudRepository<Product, Long> {

    public Product findProductByCode(String value);

    @Override
    public List<Product> findAll();

}

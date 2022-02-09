package com.wsp.webshop.service;

import com.wsp.webshop.model.Customer;
import com.wsp.webshop.model.Product;
import com.wsp.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements TempService<Product, Long> {

    @Autowired
    ProductRepository prodRepo;

    @Override
    public List<Product> getAll() {
        return prodRepo.findAll();
    }

    @Override
    public Optional<Product> getByID(Long id) {
        Optional<Product> prod = prodRepo.findById(id);
        return prod;
    }

    @Override
    public Product saveNew(Product product) {
        return prodRepo.save(product);
    }

    @Override
    public void delete(Product product) {
        prodRepo.delete(product);
    }
}

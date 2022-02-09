package com.wsp.webshop.validation;

import com.wsp.webshop.model.Product;
import com.wsp.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<UniqueValue,String> {

    @Autowired
    ProductRepository prodRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        Product prod = prodRepo.findProductByCode(value);

        if(prod != null)
            return false;
        else
            return true;

    }
}

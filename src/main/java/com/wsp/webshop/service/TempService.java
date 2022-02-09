package com.wsp.webshop.service;

import com.wsp.webshop.model.Customer;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TempService<T,ID> {

    public List<T> getAll();

     Optional<T> getByID(ID id);

    <S extends T> S saveNew(S entity);

    void delete(T entity);

}

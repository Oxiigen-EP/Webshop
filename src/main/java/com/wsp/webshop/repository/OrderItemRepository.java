package com.wsp.webshop.repository;

import com.wsp.webshop.model.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "apiorderitem", path="apiorderitem")
public interface OrderItemRepository extends CrudRepository<OrderItem,Long> {

}

package com.wsp.webshop.repository;

import com.wsp.webshop.model.WebshopOrder;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource(collectionResourceRel = "apiorders", path="apiorders")
public interface OrderRepository extends CrudRepository<WebshopOrder,Long> {


}

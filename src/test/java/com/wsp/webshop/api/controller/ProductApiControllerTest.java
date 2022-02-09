package com.wsp.webshop.api.controller;


import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.wsp.webshop.model.Customer;
import com.wsp.webshop.model.Product;
import com.wsp.webshop.service.ProductServiceImpl;
import org.apache.tomcat.jni.Proc;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ProductServiceImpl prodService;

    @ClassRule
    public static WireMockClassRule wireMockClassRule =
            new WireMockClassRule(WireMockConfiguration.wireMockConfig().dynamicPort());

    @Rule
    public WireMockClassRule methodRule = wireMockClassRule;

    @DynamicPropertySource
    static void overrideWebClientBaseUrl(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("webshop_base_url", wireMockClassRule::baseUrl);
    }

    Faker faker = new Faker();
    Random random = new Random();

    @Test
    public void getAllCustomers() {

        System.out.println("Stored stub mappings: " + wireMockClassRule.getStubMappings());

        List<Product> all_products = prodService.getAll();

        wireMockClassRule.stubFor(
                WireMock.get(urlPathEqualTo("/app-api/customers"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(Json.write(all_products)))
        );

        assertEquals(all_products.size(), 5);

    }

    @Test
    public void getProductById() {

        Product prod = new Product();
        prod.setProduct_id(Long.MAX_VALUE);
        prod.setProduct_name("Testni produkt");
        prod.setCode("1234567891");
        prod.setPrice_hrk(BigDecimal.valueOf(999.9));
        prod.setDescription("Proizvod za test dodavanje u bazu");
        prod.setIs_avaliable(true);

        String jsonBody = Json.write(prod);
        System.out.println(jsonBody);

        wireMockClassRule.stubFor(WireMock.get(urlPathMatching("/app-api/customers/9]"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        Optional<Product> product = prodService.getByID(9L);

        assertNotNull(product);
        assertEquals("Testni produkt", product.get().getProduct_name());
        assertEquals("1234567891", product.get().getCode());
        assertEquals(BigDecimal.valueOf(999.9).setScale(2), product.get().getPrice_hrk());
        assertEquals("Proizvod za test dodavanje u bazu", product.get().getDescription());
        assertEquals(true, product.get().getIs_avaliable());


    }

    @Test
    public void createNewProduct() {


        DecimalFormat df2 = new DecimalFormat("####.##");
        double min = 0;
        double max = 9999.99;
        double diff = max - min;
        double randomValue = min + Math.random( ) * diff;
        System.out.println(df2.format(randomValue));



        String code = faker.code().isbn10();
        String productName = faker.esports().game();
        BigDecimal priceInHRK = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
        String productDescription = faker.esports().event();
        Boolean isAvail = random.nextBoolean();

        Product prod = new Product();
        prod.setProduct_name(productName);
        prod.setCode(code);
        prod.setPrice_hrk(priceInHRK);
        prod.setDescription(productDescription);
        prod.setIs_avaliable(isAvail);

        String jsonBody = Json.write(prod);
        System.out.println(jsonBody);

        wireMockClassRule.stubFor(post(urlPathEqualTo("/app-api/product")).willReturn(
                aResponse().withStatus(201).withHeader("Content-Type", "application/json").withBody(jsonBody)));

        Product newProduct = prodService.saveNew(prod);

        // assertEquals(Optional.ofNullable(newCustomer.getCustomer_id()), Optional.ofNullable(4));
        assertEquals(newProduct.getCode(),code);
        assertEquals(newProduct.getProduct_name(),productName);
        assertEquals(newProduct.getPrice_hrk(),priceInHRK);
        assertEquals(newProduct.getDescription(),productDescription);
        assertEquals(newProduct.getIs_avaliable(),isAvail);


        String jsonBodyFinal = Json.write(newProduct);
        System.out.println(jsonBodyFinal);

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createNewProductCodeExist() {

        Optional<Product> product = prodService.getByID(30L);

        if(product.isPresent()){

            String productName = faker.esports().game();
           // BigDecimal priceInHRK = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
            String productDescription = faker.esports().event();
            Boolean isAvail = random.nextBoolean();

            Product prod = new Product();
            prod.setProduct_name(productName);
            prod.setCode(product.get().getCode());
            prod.setPrice_hrk(product.get().getPrice_hrk());
            prod.setDescription(productDescription);
            prod.setIs_avaliable(isAvail);

            String jsonBody = Json.write(prod);
            System.out.println(jsonBody);

            wireMockClassRule.stubFor(post(urlPathEqualTo("/app-api/products")).willReturn(
                    aResponse().withStatus(207).withHeader("Content-Type", "application/json").withBody(jsonBody)));


           Product newProduct = prodService.saveNew(prod);

        }

    }

    @Test
    public void deleteProduct() {

        Long prodId = 49L;

        Optional<Product> prod = prodService.getByID(prodId);

        if (prod.isPresent()) {
          //  Product prodDeleted = new Product();

            wireMockClassRule.stubFor(delete(urlPathEqualTo("/app-api/products/" + prodId)).willReturn(
                    aResponse().withStatus(204).withHeader("Content-Type", "application/json").withBody(Json.write(prod.get()))));

            System.out.println(Json.write(prod.get()));

           //prodService.delete(prod.get());

            this.webTestClient
                    .delete()
                    .uri("/app-api/products/" + prodId)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);

            System.out.println("Artikl obrisan!");

        } else {

            wireMockClassRule.stubFor(delete(urlPathEqualTo("/app-api/products/" + prodId)).willReturn(
                    aResponse().withStatus(207).withHeader("Content-Type", "application/json").withBody("[]")));

            this.webTestClient
                    .delete()
                    .uri("/app-api/products/" + prodId)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.MULTI_STATUS);
            System.out.println("Ne postoji artikl");
        }

    }

}

package com.wsp.webshop.api.controller;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.wsp.webshop.exception.ApiException;
import com.wsp.webshop.model.Customer;
import com.wsp.webshop.repository.CustomerRepository;
import com.wsp.webshop.service.CustomerService;
import com.wsp.webshop.service.CustomerServiceImpl;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

 public class CustomerApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    CustomerServiceImpl custService;

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

  /*  @Test
    public void getAllCustomers() {

        System.out.println("Stored stub mappings: " + wireMockClassRule.getStubMappings());

        wireMockClassRule.stubFor(
                WireMock.get("/app-api/customers")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[]"))
        );

        this.webTestClient
                .get()
                .uri("/app-api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.length()").isEqualTo(2);
    }*/

    @Test
    public void getAllCustomers() {

        System.out.println("Stored stub mappings: " + wireMockClassRule.getStubMappings());

        List<Customer> all_customers = custService.getAll();

        wireMockClassRule.stubFor(
                WireMock.get(urlPathEqualTo("/app-api/customers"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(Json.write(all_customers)))
        );

        assertEquals(all_customers.size(), 2);

    }

    @Test
    public void getCustomerById() {

        Customer cust = new Customer();
        cust.setCustomer_id(Long.MAX_VALUE);
        cust.setFirst_name("Ivan");
        cust.setLast_name("Ivanič");
        cust.setEmail("ivantest@gmail.com");

        String jsonBody = Json.write(cust);
        System.out.println(jsonBody);

        wireMockClassRule.stubFor(WireMock.get(urlPathMatching("/app-api/customers/1]"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(Json.write(cust))));

        Optional<Customer> customer = custService.getByID(1L);

        assertNotNull(customer);
        assertEquals("Ivan", customer.get().getFirst_name());
        assertEquals("Ivanič", customer.get().getLast_name());
    }

    @Test
    public void createNewCustomer() {

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + lastName + "@gmail.com";


        Customer cust = new Customer();
        cust.setFirst_name(firstName);
        cust.setLast_name(lastName);
        cust.setEmail(email);

        wireMockClassRule.stubFor(post(urlPathEqualTo("/app-api/customer")).willReturn(
                aResponse().withStatus(201).withHeader("Content-Type", "application/json").withBody(Json.write(cust))));

        Customer newCustomer = custService.saveNew(cust);

        // assertEquals(Optional.ofNullable(newCustomer.getCustomer_id()), Optional.ofNullable(4));
        assertEquals(newCustomer.getFirst_name(), firstName);
        assertEquals(newCustomer.getLast_name(), lastName);
        assertEquals(newCustomer.getEmail(), email);

        String jsonBody = Json.write(newCustomer);
        System.out.println(jsonBody);

    }

    @Test
    public void deleteCustomer() {

        Long custId = 2L;

        Optional<Customer> cust = custService.getByID(custId);

        if (cust.isPresent()) {
            Customer custDeleted = new Customer();
            custDeleted.setCustomer_id(cust.get().getCustomer_id());
            custDeleted.setFirst_name(cust.get().getFirst_name());
            custDeleted.setLast_name(cust.get().getLast_name());
            custDeleted.setEmail(cust.get().getEmail());

            wireMockClassRule.stubFor(delete(urlPathEqualTo("/app-api/customers/" + custId)).willReturn(
                    aResponse().withStatus(204).withHeader("Content-Type", "application/json").withBody(Json.write(custDeleted))));

           // custService.delete(custDeleted);

            this.webTestClient
                    .delete()
                    .uri("/app-api/customers/" + custId)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);

            System.out.println("Korisnik obrisan!");

        } else {

            wireMockClassRule.stubFor(delete(urlPathEqualTo("/app-api/customers/" + custId)).willReturn(
                    aResponse().withStatus(207).withHeader("Content-Type", "application/json").withBody("[]")));

            this.webTestClient
                    .delete()
                    .uri("/app-api/customers/" + custId)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.MULTI_STATUS);
            System.out.println("Ne postoji korisnik");
        }

    }

    @Test
    public void updateCustomer(){

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + lastName + "@gmail.com";

        Long custId = 8L;

        Optional<Customer> cust = custService.getByID(custId);
        if (cust.isPresent()) {
            cust.get().setFirst_name(firstName);
            cust.get().setLast_name(lastName);
            cust.get().setEmail(email);

            String jsonCustomer = Json.write(cust.get());
            System.out.println(jsonCustomer);

            wireMockClassRule.stubFor(patch(urlPathEqualTo("/app-api/customers/" + custId)).willReturn(aResponse().withStatus(200)
                    .withHeader("Content-Type", "application/json").withBody(jsonCustomer)));

            Customer newCustomer = custService.saveNew(cust.get());

            assertEquals(newCustomer.getCustomer_id(), custId);
            assertEquals(newCustomer.getFirst_name(), firstName);
            assertEquals(newCustomer.getLast_name(), lastName);
            assertEquals(newCustomer.getEmail(), email);


        } else{

            wireMockClassRule.stubFor(patch(urlPathEqualTo("/app-api/customers/" + custId)).willReturn(
                    aResponse().withStatus(207).withHeader("Content-Type", "application/json").withBody("[]")));

            this.webTestClient
                    .delete()
                    .uri("/app-api/customers/" + custId)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.MULTI_STATUS);

            System.out.println("Ne postoji korisnik s ID: "+custId);
        }
    }
}

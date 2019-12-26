package com.multitenant.arc;

import com.multitenant.arc.config.Constants;
import com.multitenant.arc.config.multitenant.TenantContext;
import com.multitenant.arc.dto.AuthenticationRequest;
import com.multitenant.arc.dto.AuthenticationResponse;
import com.multitenant.arc.model.Customer;
import com.multitenant.arc.model.User;
import com.multitenant.arc.security.TokenHelper;
import com.multitenant.arc.service.CustomerService;
import com.multitenant.arc.service.UserService;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultitenantApplicationTests {

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenHelper tokenHelper;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    private String BASE_URL = "http://localhost:";

    private String URL_PREFIX = "/altimetrik";

    private String TOKEN_PREFIX = "Bearer ";

    private User bhavinUser = new User("bhavin", "mypassword", "google");
    private User ranvirUser = new User("ranvir", "akkiPassword", "google");
    private User dipikaUser = new User("dipika", "depikaPass", "apple");

    @BeforeEach
    public void insertDummyData() {
        bhavinUser.setId(userService.addUser(bhavinUser.clone()).getId());
        ranvirUser.setId(userService.addUser(ranvirUser.clone()).getId());
        dipikaUser.setId(userService.addUser(dipikaUser.clone()).getId());
    }

    @AfterEach
    public void cleanUpUserData() {
        userService.cleanUp();
    }

    private void cleanUpCustomerData(String tenant){
        TenantContext.setCurrentTenant(tenant);
        customerService.cleanUp();
        TenantContext.clearTenant();
    }

    private String generateToken(AuthenticationRequest authenticationRequest) throws URISyntaxException {

        final String baseUrl = BASE_URL + randomServerPort + URL_PREFIX + Constants.GENERATE_TOKEN_URL;
        URI uri = new URI(baseUrl);

        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authenticationRequest);

        ResponseEntity<AuthenticationResponse> result = restTemplate.postForEntity(uri, request, AuthenticationResponse.class);

        if (result.getStatusCodeValue() != HttpStatus.ACCEPTED.value()
                || result.getBody() == null
                || !result.getBody().getUser().getUsername().equals(authenticationRequest.getUserName())
                || result.getBody().getToken() == null) {

            Assert.fail();
        }

        return result.getBody().getToken();
    }

    private ResponseEntity<Customer> addCustomer(String token, Customer customer) throws URISyntaxException {
        final String baseUrl = BASE_URL + randomServerPort + URL_PREFIX + Constants.ADD_CUSTOMER_URL;
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, token);

        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

        return restTemplate.postForEntity(uri, request, Customer.class);
    }

    @Test
    public void testAddUser() throws URISyntaxException {
        final String baseUrl = BASE_URL + randomServerPort + URL_PREFIX + Constants.ADD_USER_URL;
        URI uri = new URI(baseUrl);

        User user = new User("kareena", "kareenaPass", "google");

        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<User> response = restTemplate.postForEntity(uri, request, User.class);

        Assert.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(user.getUsername(), response.getBody().getUsername());
        Assert.assertEquals(user.getTenant(), response.getBody().getTenant());

    }

    @Test
    public void testAddCustomerOne() throws URISyntaxException {

        final String token = generateToken(new AuthenticationRequest(bhavinUser.getUsername(), bhavinUser.getPassword()));
        Customer customer = new Customer("Salman Khan", "This is Salman's address");

        ResponseEntity<Customer> response = addCustomer(TOKEN_PREFIX + token, customer);

        Assert.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getId());
        Assert.assertEquals(customer.getName(), response.getBody().getName());
        Assert.assertEquals(customer.getAddress(), response.getBody().getAddress());

        cleanUpCustomerData(tokenHelper.getTenantFromToken(token));
    }

    @Test
    public void testAddCustomerTwo() throws URISyntaxException {

        final String token = generateToken(new AuthenticationRequest(dipikaUser.getUsername(), dipikaUser.getPassword()));
        Customer customer = new Customer("Ranbir Kapoor", "This is Ranbir's address");

        ResponseEntity<Customer> response = addCustomer(TOKEN_PREFIX + token, customer);

        Assert.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getId());
        Assert.assertEquals(customer.getName(), response.getBody().getName());
        Assert.assertEquals(customer.getAddress(), response.getBody().getAddress());

        cleanUpCustomerData(tokenHelper.getTenantFromToken(token));
    }

    @Test
    public void testGetCustomer() throws URISyntaxException {
        final String token = generateToken(new AuthenticationRequest(dipikaUser.getUsername(), dipikaUser.getPassword()));

        Customer createdCustomer = addCustomer(TOKEN_PREFIX + token, new Customer("Saif Ali Khan", "This is Saif's address")).getBody();
        if (createdCustomer == null) {
            Assert.fail();
        }

        final String baseUrl = BASE_URL + randomServerPort + URL_PREFIX + Constants.GET_CUSTOMER_URL + "/" + createdCustomer.getId();
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, TOKEN_PREFIX + token);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Customer> response = restTemplate.exchange(uri, HttpMethod.GET, request, Customer.class);

        Assert.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(createdCustomer.getId(), response.getBody().getId());
        Assert.assertEquals(createdCustomer.getName(), response.getBody().getName());
        Assert.assertEquals(createdCustomer.getAddress(), response.getBody().getAddress());

        cleanUpCustomerData(tokenHelper.getTenantFromToken(token));
    }
}

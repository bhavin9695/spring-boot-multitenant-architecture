package com.multitenant.arc.controller;

import com.multitenant.arc.config.Constants;
import com.multitenant.arc.dto.AuthValidationRequest;
import com.multitenant.arc.dto.AuthenticationRequest;
import com.multitenant.arc.model.Customer;
import com.multitenant.arc.model.User;
import com.multitenant.arc.service.AuthService;
import com.multitenant.arc.service.CustomerService;
import com.multitenant.arc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@RestController
@RequestMapping(Constants.API_URL)
public class MultitenantController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantController.class);

    /**
     * This is a end point for adding new user
     *
     * @param user : user object with required value
     * */

    @PostMapping(value = Constants.ADD_USER_URL, headers = "Accept=application/json")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            LOGGER.info("Got request for /add/user . Request data : {}", user);
            return new ResponseEntity<>(userService.addUser(user), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point to generate new token
     *
     * @param authenticationRequest : AuthenticationRequest object with required value
     * */
    @PostMapping(value = Constants.GENERATE_TOKEN_URL, headers = "Accept=application/json")
    public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            LOGGER.info("Got request for /generate/token . Request data : {}", authenticationRequest);
            return new ResponseEntity<>((authService.authoriseAndGenerateToken(authenticationRequest)), HttpStatus.ACCEPTED);
        } catch (AuthenticationException e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point to validate existing token
     *
     * @param authValidationRequest : AuthValidationRequest object with token value
     * */
    @PostMapping(value = Constants.VALIDATE_TOKEN_URL, headers = "Accept=application/json")
    public ResponseEntity<?> validateToken(@RequestBody AuthValidationRequest authValidationRequest) {
        try {
            LOGGER.info("Got request for /validate/token ");
            return new ResponseEntity<>((authService.validateToken(authValidationRequest)), HttpStatus.ACCEPTED);
        } catch (AuthenticationException e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point for adding new customer.
     * @param customer : Customer object with required value.
     *
     * */
    @PostMapping(value = Constants.ADD_CUSTOMER_URL, headers = "Accept=application/json")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        try {
            LOGGER.info("Got request for /add/customer . Request data : {}", customer);
            return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point to get existing customer base on customer id.
     * @param customerId : Customer id which you want get.
     * */
    @GetMapping(value = Constants.GET_CUSTOMER_URL + "/{customerId}", headers = "Accept=application/json")
    public ResponseEntity<?> getCustomer(@PathVariable("customerId") Long customerId) {
        try {
            LOGGER.info("Got request for /get/customer/{customerId} . Path variable : {}", customerId);
            return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point to get all existing customer.
     * */
    @GetMapping(value = Constants.GET_ALL_CUSTOMER_URL, headers = "Accept=application/json")
    public ResponseEntity<?> getAllCustomer() {
        try {
            LOGGER.info("Got request for /get/all/customer");
            return new ResponseEntity<>(customerService.getAllCustomer(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This is a end point update existing customer object.
     * @param customer : This will update existing customer based on customer id.
     * */
    @PutMapping(value = Constants.UPDATE_CUSTOMER_URL, headers = "Accept=application/json")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            LOGGER.info("Got request for /update/customer . Request Data : {}", customer);
            return new ResponseEntity<>(customerService.updateCustomer(customer), HttpStatus.ACCEPTED);
        }  catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * This is a end point to delete existing customer.
     * @param customerId : Customer id which you want to delete.
     * */
    @DeleteMapping(value = Constants.DELETE_CUSTOMER_URL + "/{customerId}", headers = "Accept=application/json")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") Long customerId) {
        try {
            LOGGER.info("Got request for /get/customer/{customerId} . Path variable : {}", customerId);
            return new ResponseEntity<>(customerService.deleteCustomer(customerId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * This is a end point to upload customer data from excel file.
     * @param excelFile : Excel data file with customer detail
     *
     * */
    @PostMapping(value = Constants.READ_EXCEL_URL, headers = "Accept=application/json")
    public ResponseEntity<?> readExcelFile(@RequestParam("file") MultipartFile excelFile) {
        try {
            LOGGER.info("Got request for /read/excel.");
            return new ResponseEntity<>(customerService.addCustomersFromExcel(excelFile), HttpStatus.ACCEPTED);
        } catch (IOException e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error occurred : {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

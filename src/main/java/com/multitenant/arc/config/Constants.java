package com.multitenant.arc.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String DEFAULT_TENANT = "altimetrik";
    public static final String STATUS_ACTIVE = "active";
    public static final String NAME_CELL = "Name";
    public static final String ADDRESS_CELL = "Address";
    public static final String API_URL = "/altimetrik";
    public static final String ADD_USER_URL = "/add/user";
    public static final String GENERATE_TOKEN_URL = "/generate/token";
    public static final String VALIDATE_TOKEN_URL = "/validate/token";
    public static final String ADD_CUSTOMER_URL = "/add/customer";
    public static final String GET_CUSTOMER_URL = "/get/customer";
    public static final String GET_ALL_CUSTOMER_URL = "/get/all/customer";
    public static final String UPDATE_CUSTOMER_URL = "/update/customer";
    public static final String DELETE_CUSTOMER_URL = "/delete/customer";
    public static final String READ_EXCEL_URL = "/read/excel";
    public static final List<String> ALLOW_URL = new ArrayList<String>(Arrays.asList(ADD_USER_URL, GENERATE_TOKEN_URL, VALIDATE_TOKEN_URL));
}

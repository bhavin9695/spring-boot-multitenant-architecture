package com.altimetrik.assignment.config;

import com.altimetrik.assignment.core.exceptions.TokenExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TokenExpirationHandler {
   /* @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public  handler() {
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("status", "error");
        m1.put("message", "Sorry, your provided token information expired or not exists.");
        return m1;
    }*/
}

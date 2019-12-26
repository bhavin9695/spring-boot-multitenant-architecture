package com.multitenant.arc.config;

import org.springframework.web.bind.annotation.ControllerAdvice;

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

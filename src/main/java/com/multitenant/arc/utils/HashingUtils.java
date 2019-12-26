package com.multitenant.arc.utils;


import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;


public class HashingUtils {

    /**
     * It will generate MD5 hash value.
     * @param value : any string value for which you want to generate hash value.
     * @return : it will return generated hash value
     * */
    public static String generateHash(String value){
        return DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * This will generate hash value of rawString and will compare with encodedString
     * It will check either of rawString or encodedString is null then it will throw exception.
     *
     * @param rawString : Any string value which you want to compare with encodedString.
     * @param encodedString : Encoded string which will be compared.
     * */
    public static boolean compare(String rawString, String encodedString) throws IllegalStateException{
        if(rawString == null){
            throw new IllegalStateException("Raw string is null");
        }

        if(encodedString == null){
            throw new IllegalStateException("Encoded string is null");
        }
        String encodedRawValue = generateHash(rawString);

        return encodedRawValue.equals(encodedString);

    }
}

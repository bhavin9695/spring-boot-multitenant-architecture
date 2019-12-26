package com.altimetrik.assignment.service;

import com.altimetrik.assignment.core.exceptions.BadCredentialException;
import com.altimetrik.assignment.core.exceptions.TokenExpiredException;
import com.altimetrik.assignment.core.exceptions.UsernameNotFoundException;
import com.altimetrik.assignment.dto.AuthValidationRequest;
import com.altimetrik.assignment.dto.AuthValidationResponse;
import com.altimetrik.assignment.dto.AuthenticationRequest;
import com.altimetrik.assignment.dto.AuthenticationResponse;
import com.altimetrik.assignment.model.User;
import com.altimetrik.assignment.repository.UserRepo;
import com.altimetrik.assignment.security.TokenHelper;
import com.altimetrik.assignment.utils.HashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.security.sasl.AuthenticationException;


@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenHelper tokenHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    /**
     * This will authenticate user and will generate token
     * @param authenticationRequest: it contains username and password value for authentication.
     *
     * @return : it will return Authentication response with token and user object.
     * */
    public AuthenticationResponse authoriseAndGenerateToken(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        User user= userRepo.findByUsername(authenticationRequest.getUserName());
        if(user == null){
            LOGGER.error("Username : {} not found", authenticationRequest.getUserName());
            throw new UsernameNotFoundException("Username : " + authenticationRequest.getUserName() + " not found");
        }

        if(!HashingUtils.compare(authenticationRequest.getPassword(), user.getPassword())){
            LOGGER.error("Wrong credentials.");
            throw new BadCredentialException("Incorrect credentials provided");
        }

        AuthenticationResponse response = new AuthenticationResponse();
        response.setUser(user);
        String token = tokenHelper.generateToken(user.getUsername(), user.getTenant());
        LOGGER.debug("Token generated.");
        response.setToken(token);
        return response;
    }

    /**
     * This will validate token expiration.
     * @param authValidationRequest: AuthValidationRequest object with token value
     *
     * @return : it will return Authentication response with token and user object.
     *
     * */
    public AuthenticationResponse validateToken(AuthValidationRequest authValidationRequest) throws TokenExpiredException {
        boolean isExpired = tokenHelper.isExpired(authValidationRequest.getToken());
        if(isExpired){
            throw  new TokenExpiredException("Token expired. Please login again");
        }

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(authValidationRequest.getToken());

        String username = tokenHelper.getUsernameFromToken(authValidationRequest.getToken());
        User user = userRepo.findByUsername(username);

        response.setUser(user);

        return response;
    }
}

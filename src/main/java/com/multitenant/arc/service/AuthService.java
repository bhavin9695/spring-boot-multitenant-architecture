package com.multitenant.arc.service;

import com.multitenant.arc.core.exceptions.BadCredentialException;
import com.multitenant.arc.core.exceptions.TokenExpiredException;
import com.multitenant.arc.core.exceptions.UsernameNotFoundException;
import com.multitenant.arc.dto.AuthValidationRequest;
import com.multitenant.arc.dto.AuthenticationRequest;
import com.multitenant.arc.dto.AuthenticationResponse;
import com.multitenant.arc.model.User;
import com.multitenant.arc.repository.UserRepo;
import com.multitenant.arc.security.TokenHelper;
import com.multitenant.arc.utils.HashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

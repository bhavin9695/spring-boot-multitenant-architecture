package com.multitenant.arc.service;

import com.multitenant.arc.model.User;
import com.multitenant.arc.repository.UserRepo;
import com.multitenant.arc.utils.HashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepo userRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * This will add new user. It will check existing user with same username.
     * If it will find same username, then it will throw error.
     * @param user : User object with required data.
     * @return : Newly created user object.
     * */
    public User addUser(User user) throws IllegalStateException {

        User tempUser = userRepo.findByUsername(user.getUsername());

        if(tempUser != null){
            LOGGER.error("Username {} already registered.", user.getUsername());
            throw new IllegalStateException("Username already registered. Please use another username");
        }

        user.setPassword(HashingUtils.generateHash(user.getPassword()));
        return userRepo.save(user);
    }

    /**
     * This will clean user table.
     * */
    public void cleanUp(){
        userRepo.deleteAll();
        LOGGER.info("Cleaned User table");
    }

}

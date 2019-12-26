package com.multitenant.arc.repository;

import com.multitenant.arc.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    User findByUsername(String userName);
}

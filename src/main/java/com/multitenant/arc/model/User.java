package com.multitenant.arc.model;

import com.multitenant.arc.config.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String tenant;
    @Column(nullable = false)
    private String status = Constants.STATUS_ACTIVE;

    public User() {
    }

    public User(String username, String password, String tenant) {
        this.username = username;
        this.password = password;
        this.tenant = tenant;
        this.status = status;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //@JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(tenant, user.tenant) &&
                Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, tenant, status);
    }

    @Override
    public User clone()
    {
        User user = new User(this.username, this.password, this.tenant);
        user.id = this.id;
        user.status = this.status;

        return user;
    }
}

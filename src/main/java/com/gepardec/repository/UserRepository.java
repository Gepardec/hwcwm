package com.gepardec.repository;

import com.gepardec.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserRepository implements PanacheRepository<User> {

    public User getUser(String username) {
        return find("username", username).firstResult();
    }
}

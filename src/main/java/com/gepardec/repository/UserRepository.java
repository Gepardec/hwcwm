package com.gepardec.repository;

import com.gepardec.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@RequestScoped
public class UserRepository implements PanacheRepository<User> {

    public User getUser(String username) {

        return find("username", username)
                .firstResultOptional()
                .orElseThrow(() -> new NoResultException(String.format("User with Username %s not found.", username)));
    }
}

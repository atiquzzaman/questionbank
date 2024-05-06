package com.questionbank.repository;

import com.questionbank.entity.User;
import com.questionbank.entity.UserType;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepository extends BaseRepository<User> {
    public Optional<User> findByEmail(String email) {
        return queryManager.createQuery("SELECT u FROM User u WHERE u.active = true AND u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst();
    }

    public List<User> findUsersByActive(boolean active) {
        logger.info("Fetching user by active {}", active);
        return queryManager.createQuery("SELECT u FROM User u WHERE u.active = :active AND u.type != :type", User.class)
                .setParameter("active", active)
                .setParameter("type", UserType.ADMIN)
                .getResultList();
    }
}

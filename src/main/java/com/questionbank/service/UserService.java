package com.questionbank.service;

import com.questionbank.entity.User;
import com.questionbank.repository.UserRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserService extends BaseService<User> {

    @EJB
    UserRepository userRepository;

    public Optional<User> getUser(Long id) {
        logger.info("Fetching user by ID {}", id);
        return userRepository.findById(User.class, id);
    }

    public Optional<User> getUser(String email) {
        logger.info("Fetching user by email {}", email);
        return userRepository.findByEmail(email);
    }

    public User saveOrUpdate(User user) {
        setAuditFields(user);
        if (user.getId() != null && user.getId() > 0) {
            logger.info("Updating user {}", user);
            return userRepository.update(user);
        } else {
            logger.info("Creating user {}", user);
            return userRepository.create(user);
        }
    }

    public List<User> findActiveUsers() {
        logger.info("Finding all active users");
        return userRepository.findUsersByActive(true);
    }
}

package com.questionbank.view;

import com.questionbank.auth.AuthUtils;
import com.questionbank.entity.User;
import com.questionbank.entity.UserType;
import com.questionbank.service.UserService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.LazyDataModel;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class UserView extends BaseView {
    @EJB
    UserService userService;

    private Long userId;
    private User user;
    private List<User> userList;
    private LazyDataModel<User> lazyDataModel;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    public LazyDataModel<User> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<User> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public void submit() {
        logger.info("Saving user {}", user);
        if (user.getId() == null) {
            logger.info("New user, setting default password {}", user);
            try {
                String salt = AuthUtils.generateSalt();
                String hash = AuthUtils.hashPass("pass123", salt);
                user.setPasswordSalt(salt);
                user.setPasswordHash(hash);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.error("Password could not be set due to error", e);
                throw new RuntimeException(e);
            }
        }
        String successMessage = "User has been saved";
        if (user.getId() != null && user.getId() > 0) {
            successMessage = "User has been updated";
        }
        userService.saveOrUpdate(user);
        showSuccessGrowl(successMessage);
    }

    public void loadUserForm() {
        if (userId != null && userId > 0) {
            logger.info("Loading user by ID: {}", userId);
            Optional<User> optionalUser = userService.getUser(userId);
            if (optionalUser.isEmpty()) {
                logger.warn("User could not be found by ID: {}. Throwing error", userId);
            } else {
                user = optionalUser.get();
                logger.info("User was found {}", user);

            }
        } else {
            logger.info("Loading new user form");
            user = new User();
            user.setType(UserType.GENERAL);
            user.setActive(true);
        }
    }

    public void loadUserLazyData() {
        logger.info("Loading user lazy data");
        this.lazyDataModel = userService.getLazyDataModel(User.class);
    }

    public void toggleActivation() {
        user.setActive(!user.isActive());
        logger.info("Set user active: {} for user: {}", user.isActive(), user.getId());
        userService.saveOrUpdate(user);
        showSuccessGrowl(user.isActive() ? "User has been activated" : "User has been deactivated");
    }
}

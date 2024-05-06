package com.questionbank.auth;

import com.questionbank.entity.Category;
import com.questionbank.entity.User;
import com.questionbank.entity.UserType;
import com.questionbank.service.CategoryService;
import com.questionbank.service.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class UserServiceIdentityStore implements IdentityStore {
    @EJB
    private UserService userService;

    @EJB
    CategoryService categoryService;

    Logger logger = LogManager.getLogger(UserServiceIdentityStore.class);

    // validate is not called, when there's already a caller in the securityContext
    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;
        String callerId = login.getCaller();
        String password = login.getPasswordAsString();

        Optional<User> optionalUser = userService.getUser(callerId);

        if (optionalUser.isEmpty()) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        User user = optionalUser.get();

        try {
            if (!AuthUtils.matchPass(user.getPasswordHash(), password, user.getPasswordSalt())) {
                return CredentialValidationResult.INVALID_RESULT;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        Set<String> roleSet = new HashSet<>();
        if (UserType.ADMIN.equals(user.getType())) {
            roleSet.add(UserType.ADMIN.name());
        } else {
            roleSet.add(UserType.GENERAL.name());
        }

        List<Category> categoryForContributor = categoryService.findByContributor(user);
        List<Category> categoryForReviewer = categoryService.findByReviewer(user);
        UserDetails userDetails = new UserDetails(user, categoryForContributor, categoryForReviewer);

        return new CredentialValidationResult(new UserPrincipal(userDetails), roleSet);
    }
}

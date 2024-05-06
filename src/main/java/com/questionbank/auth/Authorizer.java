package com.questionbank.auth;

import com.questionbank.entity.UserType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

import java.util.Optional;

@Named
@RequestScoped
public class Authorizer {
    @Inject
    SecurityContext securityContext;

    public UserDetails getUserDetails() {
        Optional<UserDetails> optionalUser = securityContext
                .getPrincipalsByType(UserPrincipal.class)
                .stream()
                .map(UserPrincipal::getUserDetails)
                .findFirst();
        return optionalUser.orElse(null);
    }

    public boolean isContributor(Long categoryId) {
        return getUserDetails().getCategoryForContribution()
                        .stream().anyMatch(c -> c.getId().equals(categoryId));
    }

    public boolean isReviewer(Long categoryId) {
        return getUserDetails().getCategoryForReview()
                .stream().anyMatch(c -> c.getId().equals(categoryId));
    }

    public boolean isAdmin() {
        return securityContext.isCallerInRole(UserType.ADMIN.name());
    }

    public boolean isMember() {
        return securityContext.isCallerInRole(UserType.GENERAL.name());
    }
}

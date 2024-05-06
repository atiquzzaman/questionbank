package com.questionbank.auth;

import jakarta.security.enterprise.CallerPrincipal;

public class UserPrincipal extends CallerPrincipal {
    private final UserDetails userDetails;
    public UserPrincipal(UserDetails userDetails) {
        super(userDetails.getUser().getEmail());
        this.userDetails = userDetails;
    }

    public UserDetails getUserDetails() {
        return  userDetails;
    }
}

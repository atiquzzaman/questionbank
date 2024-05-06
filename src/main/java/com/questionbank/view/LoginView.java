package com.questionbank.view;

import com.questionbank.auth.UserDetails;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Named
@RequestScoped
public class LoginView extends BaseView {
    @Inject
    private HttpServletRequest request;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() throws IOException, ServletException {
        if (email == null || password == null) {
            showErrorGrowl("Please type correct email and password");
            return;
        }
        logger.info("Sign in attempts for email: {}", email);
        if (securityContext.getCallerPrincipal() != null) {
            clearSession();
        }
        switch (processAuthentication()) {
            case SEND_CONTINUE -> {
                logger.info("Sign in successful for email: {}. Forwarding URL", email);
                facesContext.responseComplete();
            }
            case SEND_FAILURE -> {
                logger.info("Invalid credentials for email: {}", email);
                showErrorGrowl("Incorrect email or password");
            }
            case SUCCESS -> {
                logger.info("Sign in successful for email: {}", email);
                UserDetails userDetails = authorizer.getUserDetails();
                if (authorizer.isAdmin()) {
                    externalContext.redirect("/secured/category/list");
                } else if (userDetails.getCategoryForReview() != null
                        && userDetails.getCategoryForReview().size() > 0) {
                    String url = String.format("/secured/question/list?categoryId=%s&listFor=%s",
                            userDetails.getCategoryForReview().get(0).getId(), QuestionListFor.REVIEWER);
                    externalContext.redirect(url);
                } else if (userDetails.getCategoryForContribution() != null
                        && userDetails.getCategoryForContribution().size() > 0) {
                    String url = String.format("/secured/question/list?categoryId=%s&listFor=%s",
                            userDetails.getCategoryForContribution().get(0).getId(), QuestionListFor.CONTRIBUTOR);
                    externalContext.redirect(url);
                } else {
                    externalContext.redirect("/secured/question/no-category");
                }
            }
        }
    }

    private AuthenticationStatus processAuthentication() {
        return securityContext.authenticate((HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(email, password)));
    }

    private void clearSession() throws ServletException {
        request.logout();
        request.getSession().invalidate();
    }

    public String logout() throws ServletException {
        logger.info("Logout attempt for username: {}.", securityContext.getCallerPrincipal().getName());
        clearSession();
        logger.info("User logged out successfully. Forwarding URL");
        return "/login?faces-redirect=true";
    }
}

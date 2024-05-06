package com.questionbank.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
@CustomFormAuthenticationMechanismDefinition(
            loginToContinue = @LoginToContinue(
                    errorPage = "",
                    useForwardToLogin = false
            )
)

@FacesConfig @ApplicationScoped
public class ApplicationConfig {

}

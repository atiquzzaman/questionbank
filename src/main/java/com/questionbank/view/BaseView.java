package com.questionbank.view;

import com.questionbank.auth.Authorizer;
import com.questionbank.auth.UserDetails;
import com.questionbank.auth.UserPrincipal;
import com.questionbank.entity.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Optional;

public class BaseView implements Serializable {
    protected Logger logger = LogManager.getLogger(getClass());

    @Inject
    protected Authorizer authorizer;

    @Inject
    protected SecurityContext securityContext;

    @Inject
    protected FacesContext facesContext;

    @Inject
    protected ExternalContext externalContext;

    private void addGrowlMessage(FacesMessage.Severity severity, String summary, String detail) {
        facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
    }
    protected void showSuccessGrowl(String details) {
        addGrowlMessage(FacesMessage.SEVERITY_INFO, "Success", details);
    }

//    public void showWarn() {
//        addGrowlMessage(FacesMessage.SEVERITY_WARN, "Warn Message", "Message Content");
//    }

    protected void showErrorGrowl(String details) {
        addGrowlMessage(FacesMessage.SEVERITY_ERROR, "Error", details);
    }
}

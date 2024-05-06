package com.questionbank.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Named @SessionScoped
public class Localization implements Serializable {
    @Inject
    ExternalContext externalContext;
    private final String LOCALE_BN = "bn";
    private final String LOCALE_EN = "en";
    private boolean bangla = true;
    private Locale locale = new Locale(LOCALE_BN);
    private final String messageBundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();

    ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);

    @PostConstruct
    public void init() {
        Cookie localCookie = (Cookie) externalContext.getRequestCookieMap().get("locale");
        if (localCookie != null && LOCALE_EN.equals(localCookie.getValue())) {
            bangla = false;
            changeLanguage();
        }
    }

    public boolean isBangla() {
        return bangla;
    }

    public void setBangla(boolean bangla) {
        this.bangla = bangla;
    }

    public Locale getLocale() {
        return locale;
    }

    public void changeLanguage() {
        String localeName = bangla ? LOCALE_BN : LOCALE_EN;
        locale = new Locale(localeName);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        bundle = ResourceBundle.getBundle(messageBundleName, locale);
        Map<String, Object> properties = new HashMap<>();
        properties.put("maxAge", 60 * 60 * 24); // 1 day
        properties.put("path", "/");
        externalContext.addResponseCookie("locale", localeName, properties);
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }
}

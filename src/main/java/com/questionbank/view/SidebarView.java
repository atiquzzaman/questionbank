package com.questionbank.view;

import com.questionbank.auth.UserDetails;
import com.questionbank.entity.Category;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class SidebarView extends BaseView {
    private List<Category> categoryForContribution;
    private List<Category> categoryForReview;

    @PostConstruct
    public void init() {
        UserDetails userDetails = authorizer.getUserDetails();
        categoryForContribution = userDetails.getCategoryForContribution();
        categoryForReview = userDetails.getCategoryForReview();
    }

    public List<Category> getCategoryForContribution() {
        return categoryForContribution;
    }

    public List<Category> getCategoryForReview() {
        return categoryForReview;
    }
}

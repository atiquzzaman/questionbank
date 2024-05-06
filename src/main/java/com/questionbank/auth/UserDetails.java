package com.questionbank.auth;


import com.questionbank.entity.Category;
import com.questionbank.entity.User;

import java.io.Serializable;
import java.util.List;

public class UserDetails implements Serializable {
    private final User user;
    private final List<Category> categoryForContribution;
    private final List<Category> categoryForReview;


    public UserDetails(User user, List<Category> categoryForContribution, List<Category> categoryForReview) {
        this.user = user;
        this.categoryForContribution = categoryForContribution;
        this.categoryForReview = categoryForReview;
    }

    public User getUser() {
        return this.user;
    }

    public List<Category> getCategoryForContribution() {
        return categoryForContribution;
    }

    public List<Category> getCategoryForReview() {
        return categoryForReview;
    }
}

package com.questionbank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Category extends BaseEntity {
    @NotNull
    private String name;
    private String description;
    private boolean active;

    @ManyToMany
    @JoinTable(
            name="contributor",
            joinColumns=@JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> contributors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name="reviewer",
            joinColumns=@JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> reviewers = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<User> getContributors() {
        return contributors;
    }

    public void setContributors(Set<User> contributors) {
        this.contributors = contributors;
    }

    public Set<User> getReviewers() {
        return reviewers;
    }

    public void setReviewers(Set<User> reviewers) {
        this.reviewers = reviewers;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}

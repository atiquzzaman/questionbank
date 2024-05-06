package com.questionbank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Question extends BaseEntity {
    @NotNull
    private String problem;
    private String solution;
    private boolean active;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private QuestionLevel level;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private Set<ReviewerDecision> reviewerDecisions;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public QuestionLevel getLevel() {
        return level;
    }

    public void setLevel(QuestionLevel level) {
        this.level = level;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ReviewerDecision> getReviewerDecisions() {
        return reviewerDecisions;
    }

    public void setReviewerDecisions(Set<ReviewerDecision> reviewerDecisions) {
        this.reviewerDecisions = reviewerDecisions;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + getId() +
                ", problem='" + problem + '\'' +
                ", solution='" + solution + '\'' +
                ", active=" + active +
                ", level=" + level +
                ", approvalStatus=" + approvalStatus +
                ", category=" + category +
                ", reviewerDecisions=" + reviewerDecisions +
                '}';
    }
}

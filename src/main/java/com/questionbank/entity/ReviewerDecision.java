package com.questionbank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reviewer_decision")
public class ReviewerDecision extends BaseEntity {
    @ManyToOne
    private User reviewer;
    @ManyToOne
    private Question question;
    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Override
    public String toString() {
        return "ReviewerDecision{" +
                "id=" + getId() +
                ", reviewer=" + reviewer +
                ", question=" + question +
                ", approvalStatus=" + approvalStatus +
                '}';
    }
}

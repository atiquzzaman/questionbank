package com.questionbank.repository;

import com.questionbank.entity.ReviewerDecision;
import com.questionbank.entity.User;
import jakarta.ejb.Stateless;

import java.util.Optional;

@Stateless
public class ReviewerDecisionRepository extends BaseRepository<ReviewerDecision> {
    public Optional<ReviewerDecision> getByReviewer(User user) {
        return queryManager.createQuery("SELECT rd FROM ReviewerDecision rd WHERE rd.reviewer = :user", ReviewerDecision.class)
                .setParameter("user", user)
                .getResultList()
                .stream()
                .findFirst();
    }
}

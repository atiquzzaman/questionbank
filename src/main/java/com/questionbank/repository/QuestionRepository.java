package com.questionbank.repository;

import com.questionbank.entity.ApprovalStatus;
import com.questionbank.entity.Question;
import com.questionbank.entity.User;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class QuestionRepository extends BaseRepository<Question> {
    public List<Question> loadListByCategoryContributorApprovalStatus(User user, Long categoryId, ApprovalStatus approvalStatus) {
        logger.info("Fetching Category list by contributor {}", user.getId());
        return queryManager.createQuery("SELECT q FROM Question q WHERE q.active = true AND q.approvalStatus = :approvalStatus AND q.createdBy = :user AND q.category.id = :categoryId", Question.class)
                .setParameter("approvalStatus", approvalStatus)
                .setParameter("user", user)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    public List<Question> loadListByCategoryReviewerApprovalStatus(User user, Long categoryId, ApprovalStatus approvalStatus) {
        logger.info("Fetching Category list by reviewer {}", user.getId());
        return queryManager.createQuery("SELECT q FROM Question q JOIN q.reviewerDecisions  rd WHERE q.active = true AND q.category.id = :categoryId AND rd.approvalStatus = :approvalStatus AND rd.reviewer = :user", Question.class)
                .setParameter("approvalStatus", approvalStatus)
                .setParameter("user", user)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    public List<Question> loadListByCategoryApprovalStatus(Long categoryId, ApprovalStatus approvalStatus) {
        logger.info("Fetching Questions by category {} and approvalStatus {}", categoryId, approvalStatus);
        return queryManager.createQuery("SELECT q FROM Question q WHERE q.active = true AND q.approvalStatus = :approvalStatus AND q.category.id = :categoryId", Question.class)
                .setParameter("approvalStatus", approvalStatus)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }
}

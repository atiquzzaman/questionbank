package com.questionbank.service;

import com.questionbank.entity.ApprovalStatus;
import com.questionbank.entity.Question;
import com.questionbank.entity.ReviewerDecision;
import com.questionbank.entity.User;
import com.questionbank.repository.QuestionRepository;
import com.questionbank.repository.ReviewerDecisionRepository;
import com.questionbank.view.QuestionListFor;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.Optional;

@Stateless
public class QuestionService extends BaseService<Question> {
    @EJB
    private QuestionRepository questionRepository;

    @EJB
    private ReviewerDecisionRepository reviewerDecisionRepository;

    public Question saveOrUpdate(Question question) {
        setAuditFields(question);
        if (question.getId() != null && question.getId() > 0) {
            return questionRepository.update(question);
        } else {
            return questionRepository.create(question);
        }
    }

    public Optional<Question> getQuestion(Long questionId) {
        return questionRepository.findById(Question.class, questionId);
    }

    public List<Question> loadQuestionList(User user, Long categoryId, QuestionListFor questionListFor, ApprovalStatus approvalStatus) {
        if (QuestionListFor.CONTRIBUTOR.equals(questionListFor)) {
            return questionRepository.loadListByCategoryContributorApprovalStatus(user, categoryId, approvalStatus);
        } else {
            return questionRepository.loadListByCategoryReviewerApprovalStatus(user, categoryId, approvalStatus);
        }
    }

    public List<Question> loadQuestionList(Long categoryId, ApprovalStatus approvalStatus) {
        return questionRepository.loadListByCategoryApprovalStatus(categoryId, approvalStatus);
    }

    public ReviewerDecision getQuestionReviewerDecision(User user) {
        return reviewerDecisionRepository.getByReviewer(user).orElse(null);
    }

    public void updateReviewerDecision(ReviewerDecision reviewerDecision) {
        setAuditFields(reviewerDecision);
        reviewerDecisionRepository.update(reviewerDecision);
    }
}

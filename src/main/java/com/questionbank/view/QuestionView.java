package com.questionbank.view;

import com.questionbank.auth.UserDetails;
import com.questionbank.entity.*;
import com.questionbank.service.CategoryService;
import com.questionbank.service.QuestionService;
import com.questionbank.service.UserService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.event.TabChangeEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Named
@ViewScoped
public class QuestionView extends BaseView {
    @EJB
    private QuestionService questionService;

    @EJB
    CategoryService categoryService;
    private Long categoryId;
    private Long questionId;
    private QuestionListFor listFor;

    private Category category;
    private Question question;

    private ReviewerDecision reviewerDecision;

    private List<Question> questionList;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public QuestionListFor getListFor() {
        return listFor;
    }

    public void setListFor(QuestionListFor listFor) {
        this.listFor = listFor;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ReviewerDecision getReviewerDecision() {
        return reviewerDecision;
    }

    public void setReviewerDecision(ReviewerDecision reviewerDecision) {
        this.reviewerDecision = reviewerDecision;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public void loadQuestionForm() {
        UserDetails userDetails = authorizer.getUserDetails();
        User user = userDetails.getUser();
         if (categoryId != null && categoryId > 0) {
             logger.info("Showing blank form for creating a new question for category {}", categoryId);

             Optional<Category> category = userDetails.getCategoryForContribution()
                    .stream().filter(c -> c.getId().equals(categoryId)).findFirst();
            if (category.isPresent()) {
                // Initializing the question
                question = new Question();
                question.setActive(true);
                question.setApprovalStatus(ApprovalStatus.UNDECIDED);
                question.setLevel(QuestionLevel.BEGINNER);
                question.setCategory(category.get());

                // Setting category reviewers for question reviewers to track their individual decisions.
                Set<ReviewerDecision> reviewerDecisions = new HashSet<>();
                category.get().getReviewers()
                        .stream().filter(r -> !r.equals(user))
                        .forEach(r -> {
                    ReviewerDecision reviewerDecision = new ReviewerDecision();
                    reviewerDecision.setReviewer(r);
                    reviewerDecision.setQuestion(question);
                    reviewerDecision.setApprovalStatus(ApprovalStatus.UNDECIDED);
                    reviewerDecision.setCreatedBy(user);
                    reviewerDecision.setUpdatedBy(user);
                    reviewerDecisions.add(reviewerDecision);
                });
                question.setReviewerDecisions(reviewerDecisions);
            } else {
                logger.error("User {} is not allowed to create question for category {}",
                        user.getId(), categoryId);
                // todo Throw exception
            }
        } else if (questionId != null && questionId > 0) {
             logger.info("Opening question {} for editing", questionId);
             Optional<Question> optionalQuestion = questionService.getQuestion(questionId);
             if (optionalQuestion.isPresent()) {
                 question = optionalQuestion.get();
                 if (!user.equals(question.getCreatedBy())) {
                     logger.error("User {} is not allowed to edit question {}",
                             user.getId(), question.getId());
                     // todo Throw exception
                 }
             }
         } else {
             logger.error("User {} is trying to access page without questionId or categoryId",
                     user.getId());
             // todo Throw exception
         }
    }

    public void loadQuestionForReview() {
        UserDetails userDetails = authorizer.getUserDetails();
        User user = userDetails.getUser();
        if (questionId != null && questionId > 0) {
            logger.info("Opening question {} for review", questionId);
            Optional<Question> optionalQuestion = questionService.getQuestion(questionId);
            if (optionalQuestion.isPresent()) {
                question = optionalQuestion.get();
                reviewerDecision = questionService.getQuestionReviewerDecision(user);
                if (!authorizer.isAdmin() && !authorizer.isReviewer(question.getCategory().getId())) {
                    logger.error("User {} is not allowed to review question {}",
                            userDetails.getUser().getId(), question.getId());
                    // todo Throw exception
                }
            }
        } else {
            logger.error("User {} is trying to access question without questionId", user.getId());
            // todo Throw exception
        }
    }

    public void toggleActivation() {
        question.setActive(!question.isActive());
        logger.info("Set user active: {} for question: {}", question.isActive(), question.getId());
        questionService.saveOrUpdate(question);
        showSuccessGrowl(question.isActive() ? "Question has been activated" : "Question has been deactivated");
    }

    public void submit() {
        logger.info("Saving question {}", question);
        String successMessage = "Question has been saved";
        if (question.getId() != null && question.getId() > 0) {
            successMessage = "Question has been updated";
        }
        questionService.saveOrUpdate(question);
        showSuccessGrowl(successMessage);
    }

    private void loadQuestionList(ApprovalStatus approvalStatus) {
        if (categoryId == null || listFor == null) {
            logger.error("categoryId missing in URL");
            //todo throw exception
        }

        if (!authorizer.isAdmin() && listFor == null) {
            logger.error("listFor missing in URL");
            //todo throw exception
        }

        Optional<Category> optionalCategory = categoryService.getCategory(categoryId);
        category = optionalCategory.orElse(null);

        User user = authorizer.getUserDetails().getUser();
        if (!authorizer.isAdmin()) {
            logger.info("Loading question list for {}, categoryId: {}, user: {}", listFor, categoryId, user.getId());
            questionList = questionService.loadQuestionList(user, categoryId, listFor, approvalStatus);
        } else {
            logger.info("Loading question list for admin user, categoryId: {}, user: {}", categoryId, user.getId());
            questionList = questionService.loadQuestionList(categoryId, approvalStatus);
        }
        logger.info("Question list size {}", questionList.size());
    }

    public void onLoadQuestionList() {
        loadQuestionList(ApprovalStatus.UNDECIDED);
    }

    public void onTabChange(TabChangeEvent event) {
        ApprovalStatus approvalStatus = ApprovalStatus.valueOf(event.getTab().getId());
        loadQuestionList(approvalStatus);
    }

    public boolean isListForContributor() {
        return QuestionListFor.CONTRIBUTOR.equals(listFor);
    }

    public void submitFinalApproval() {
        ApprovalStatus currentStatus = question.getApprovalStatus();
        logger.info("Question before submitting final approval {}", question);
        if (!ApprovalStatus.APPROVED.equals(currentStatus)) {
            question.setApprovalStatus(ApprovalStatus.APPROVED);
        } else {
            question.setApprovalStatus(ApprovalStatus.DISAPPROVED);
        }

        logger.info("Submitting final approval {}", question);

        questionService.saveOrUpdate(question);
        showSuccessGrowl("Question has been "
                + (ApprovalStatus.APPROVED.equals(question.getApprovalStatus()) ? "approved" : "disapproved"));
    }

    public void submitReviewerDecision() {
        ApprovalStatus currentStatus = reviewerDecision.getApprovalStatus();
        if (!ApprovalStatus.APPROVED.equals(currentStatus)) {
            reviewerDecision.setApprovalStatus(ApprovalStatus.APPROVED);
        } else {
            reviewerDecision.setApprovalStatus(ApprovalStatus.DISAPPROVED);
        }

        logger.info("Saving reviewer decision {}", reviewerDecision);

        questionService.updateReviewerDecision(reviewerDecision);
        showSuccessGrowl("Question has been "
                + (ApprovalStatus.APPROVED.equals(reviewerDecision.getApprovalStatus()) ? "approved" : "disapproved"));
    }
}

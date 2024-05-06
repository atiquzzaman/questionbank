package com.questionbank.repository;

import com.questionbank.entity.Category;
import com.questionbank.entity.User;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class CategoryRepository extends BaseRepository<Category> {
    public List<Category> findByContributor(User user) {
        logger.info("Fetching Category list by contributor {}", user.getId());
        return queryManager.createQuery("SELECT c FROM Category c JOIN c.contributors con WHERE c.active = true AND con = :user", Category.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Category> findByReviewer(User user) {
        logger.info("Fetching Category list by reviewer {}", user.getId());
        return queryManager.createQuery("SELECT c FROM Category c JOIN c.reviewers r WHERE c.active = true AND r = :user", Category.class)
                .setParameter("user", user)
                .getResultList();
    }
}

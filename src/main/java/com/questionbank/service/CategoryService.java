package com.questionbank.service;

import com.questionbank.entity.Category;
import com.questionbank.entity.User;
import com.questionbank.repository.CategoryRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.Optional;

@Stateless
public class CategoryService extends BaseService<Category> {
    @EJB
    CategoryRepository categoryRepository;

    public Optional<Category> getCategory(Long categoryId) {
        logger.info("Finding category by ID {}", categoryId);
        return categoryRepository.findById(Category.class, categoryId);
    }

    public Category saveOrUpdate(Category category) {
        setAuditFields(category);
        if (category.getId() != null && category.getId() > 0) {
            logger.info("Updating category {}", category);
            return categoryRepository.update(category);
        } else {
            logger.info("Creating category {}", category);
            return categoryRepository.create(category);
        }
    }

    public List<Category> findByContributor(User user) {
        return categoryRepository.findByContributor(user);
    }

    public List<Category> findByReviewer(User user) {
        return categoryRepository.findByReviewer(user);
    }
}

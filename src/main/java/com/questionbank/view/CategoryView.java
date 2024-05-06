package com.questionbank.view;

import com.questionbank.entity.Category;
import com.questionbank.entity.User;
import com.questionbank.service.CategoryService;
import com.questionbank.service.UserService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.LazyDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Named
@ViewScoped
public class CategoryView extends BaseView {
    @EJB
    CategoryService categoryService;

    @EJB
    UserService userService;

    private LazyDataModel<Category> lazyDataModel;

    private Long categoryId;
    private Category category;
    private List<User> unassignedUserList;
    private List<User> selectedUsers;
    private boolean triggerredDialogForContributor;
    private boolean triggerredDialogForReviewer;

    public LazyDataModel<Category> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Category> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<User> getUnassignedUserList() {
        return unassignedUserList;
    }

    public void setUnassignedUserList(List<User> unassignedUserList) {
        this.unassignedUserList = unassignedUserList;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void loadUserLazyData() {
        logger.info("Loading user lazy data");
        this.lazyDataModel = categoryService.getLazyDataModel(Category.class);
    }

    public void loadCategoryForm() {
        if (categoryId != null && categoryId > 0) {
            logger.info("Loading category by ID: {}", categoryId);
            Optional<Category> optionalCategory = categoryService.getCategory(categoryId);
            if (optionalCategory.isEmpty()) {
                logger.warn("Category could be found by ID {}, throwing error", categoryId);
            } else {
                category = optionalCategory.get();
                logger.info("Category was found: {}", category);
            }
        } else {
            logger.info("New category form is loading");
            category = new Category();
            category.setActive(true);
        }
    }

    public void toggleActivation() {
        logger.info("Set category active: {} for category {}", category.isActive(), category.getId());
        category.setActive(!category.isActive());
        categoryService.saveOrUpdate(category);
        showSuccessGrowl(category.isActive() ? "Category has been activated" : "Category has been deactivated");
    }

    public void removeReviewer(User user) {
        logger.info("Removing reviewer {}", user);
        category.getReviewers().remove(user);
    }

    public void removeContributor(User user) {
        logger.info("Removing contributor {}", user);
        category.getContributors().remove(user);
    }

    public void triggerDialogAssigningContributors() {
        logger.info("Opening Dialog for adding user as contributor");
        triggerredDialogForContributor = true;
        triggerredDialogForReviewer = false;

        loadUnassignedUserList();
    }

    public void triggerDialogAssigningReviewers() {
        logger.info("Opening Dialog for adding user as reviewer");
        triggerredDialogForContributor = false;
        triggerredDialogForReviewer = true;

        loadUnassignedUserList();
    }

    public void loadUnassignedUserList() {
        logger.info("Loading all active users from database");
        unassignedUserList = userService.findActiveUsers();
        logger.info("Total {} active user found", unassignedUserList.size());

        selectedUsers.clear();

        if (triggerredDialogForContributor) {
            logger.info("Excluding already assigned contributors from active user list");
            unassignedUserList.removeAll(category.getContributors());
        } else if (triggerredDialogForReviewer) {
            logger.info("Excluding already assigned reviewers from active user list");
            unassignedUserList.removeAll(category.getReviewers());
        }
        logger.info("Total {} active users are showing", unassignedUserList.size());
    }

    public void addSelectedUsers() {
        if (triggerredDialogForContributor) {
            logger.info("Total {} users have been selected as contributor", selectedUsers.size());
            category.getContributors().addAll(selectedUsers);
        } else if (triggerredDialogForReviewer) {
            logger.info("Total {} users have been selected as reviewer", selectedUsers.size());
            category.getReviewers().addAll(selectedUsers);
        }
    }

    public void submit() {
        logger.info("Saving category {}", category);
        String successMessage = "Category has been saved";
        if (category.getId() != null && category.getId() > 0) {
            successMessage = "Category has been updated";
        }
        categoryService.saveOrUpdate(category);

        showSuccessGrowl(successMessage);
    }

    public void selectCategoryUsers(Set<User> users) {
        this.selectedUsers = new ArrayList<>(users);
    }
}

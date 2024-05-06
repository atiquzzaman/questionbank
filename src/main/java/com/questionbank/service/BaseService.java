package com.questionbank.service;

import com.questionbank.auth.Authorizer;
import com.questionbank.entity.BaseEntity;
import com.questionbank.entity.Category;
import com.questionbank.entity.User;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.internal.jpa.querydef.RootImpl;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.JpaLazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseService<T> {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Inject
    protected EntityManager queryManager;
    @Inject
    private Authorizer authorizer;
    protected void setAuditFields(BaseEntity baseEntity) {
        User currentUser = authorizer.getUserDetails().getUser();
        if (baseEntity.getId() != null && baseEntity.getId() > 0) {
            logger.info("Setting updatedBy by {}", currentUser);
            baseEntity.setUpdatedBy(currentUser);
        } else {
            logger.info("Setting createdBy and updatedBy by {}", currentUser);
            baseEntity.setCreatedBy(currentUser);
            baseEntity.setUpdatedBy(currentUser);
        }
    }

    public JpaLazyDataModel<T> getLazyDataModel(Class<T> tClass) {
        logger.info("Loading lazy data for {}", tClass.getSimpleName());
        return new JpaLazyDataModel<>(tClass, () -> queryManager);
    }
}

package com.questionbank.repository;

import com.questionbank.entity.BaseEntity;
import com.questionbank.entity.User;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BaseRepository<T extends BaseEntity> {
    protected final Logger logger = LogManager.getLogger(getClass());

    @PersistenceContext
    @Produces
    protected EntityManager queryManager;

    public Optional<T> findById(Class<T> entityClass, Long id) {
        logger.info("Fetching entity {} by ID {}", entityClass.getSimpleName(), id);
        return Optional.ofNullable(queryManager.find(entityClass, id));
    }

    public List<T> findAll(Class<T> entityClass) {
        logger.info("Fetching all data of {}", entityClass.getSimpleName());
        String queryString = String.format("SELECT o FROM %s o ORDER BY o.id", entityClass.getSimpleName());
        return queryManager.createQuery(queryString, entityClass).getResultList();
    }

    public T create(T entity) {
        logger.info("Creating entity {}", entity);
        queryManager.persist(entity);
        return entity;
    }

    public T update(T entity) {
        logger.info("Updating entity {}", entity);
        return queryManager.merge(entity);
    }
}
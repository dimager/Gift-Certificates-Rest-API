package com.epam.ems.dao.impl;

import com.epam.ems.dao.AuditDao;
import com.epam.ems.entity.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class AuditDaoImpl implements AuditDao {
    private EntityManager entityManager;

    @Autowired
    public AuditDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveAudit(Audit audit) {
        entityManager.persist(audit);
    }
}

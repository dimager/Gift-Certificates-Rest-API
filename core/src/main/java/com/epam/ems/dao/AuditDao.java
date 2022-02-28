package com.epam.ems.dao;

import com.epam.ems.entity.Audit;

public interface AuditDao {
    /**
     * Allows save audit data in db.
     *
     * @param audit audit data
     */
    void saveAudit(Audit audit);
}

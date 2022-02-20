package com.epam.ems.listener;

import com.epam.ems.dao.AuditDao;
import com.epam.ems.entity.Audit;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.Tag;
import com.epam.ems.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Service
public class AuditListener {
    static private AuditDao auditDao;

    public AuditListener() {
    }

    @Autowired
    public void init(AuditDao auditDao) {
        AuditListener.auditDao = auditDao;
    }

    @PostPersist
    public void auditPersist(Object o) {
        audit(o, "INSERT");
    }

    @PreUpdate
    void auditUpdate(Object o) {
        audit(o, "UPDATE");
    }

    @PreRemove
    void auditRemove(Object o) {
        audit(o, "DELETE");
    }

    private void audit(Object o, String operation) {
        Audit audit = new Audit();
        audit.setOperation(operation);
        audit.setOpTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        if (o instanceof Tag) {
            Tag tag = (Tag) o;
            audit.setAuditObject("tag");
            audit.setObjectId(tag.getId());
            auditDao.saveAudit(audit);
        } else if (o instanceof Certificate) {
            Certificate certificate = (Certificate) o;
            audit.setAuditObject("certificate");
            audit.setObjectId(certificate.getId());
            auditDao.saveAudit(audit);
        } else if (o instanceof Order) {
            Order order = (Order) o;
            audit.setAuditObject("order");
            audit.setObjectId(order.getId());
            auditDao.saveAudit(audit);
        } else if (o instanceof User) {
            User user = (User) o;
            audit.setAuditObject("user");
            audit.setObjectId(user.getId());
            auditDao.saveAudit(audit);
        }
    }
}

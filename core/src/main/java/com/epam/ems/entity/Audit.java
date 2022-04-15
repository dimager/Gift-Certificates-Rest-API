package com.epam.ems.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "audit")
@Data
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private long id;

    private String operation;

    @Column(name = "audit_object")
    private String auditObject;

    @Column(name = "object_id")
    private long objectId;

    @Column(name = "op_timestamp", columnDefinition = "timestamp")
    private Timestamp opTimestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audit audit = (Audit) o;
        return id == audit.id && objectId == audit.objectId && Objects.equals(operation, audit.operation) && Objects.equals(auditObject, audit.auditObject) && Objects.equals(opTimestamp, audit.opTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operation, auditObject, objectId, opTimestamp);
    }
}

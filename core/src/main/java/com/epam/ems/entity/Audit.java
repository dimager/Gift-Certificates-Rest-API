package com.epam.ems.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "audit_id")
    private long id;

    @Setter
    @Getter
    private String operation;

    @Setter
    @Getter
    @Column(name = "audit_object")
    private String auditObject;

    @Setter
    @Getter
    @Column(name = "object_id")
    private long objectId;

    @Setter
    @Getter
    @Column(name = "op_timestamp", columnDefinition = "timestamp")
    private Timestamp opTimestamp;

}

package com.epam.ems.entity;

import com.epam.ems.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "certificates")
@EntityListeners(AuditListener.class)
public class Certificate extends BaseEntity implements Comparable<Certificate>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "certificate_id")
    private long id;

    @Column(length = 45)
    private String name;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private short duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "created_date_time", columnDefinition = "timestamp")
    private Timestamp createdDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "last_update_date_time", columnDefinition = "timestamp")
    private Timestamp lastUpdatedDateTime;

    @Column(name = "image_hash")
    @JsonIgnore
    private String imageMd5Sum;

    @ManyToMany
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Valid
    @JoinTable(name = "certificate_tags",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "certificate")
    @JsonIgnore
    private List<OrderCertificate> orderCertificates = new ArrayList<>();

    @JsonIgnore
    @Column(name = "is_archived", nullable = false)
    private boolean isArchived = false;


    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createdDateTime=" + createdDateTime +
                ", lastUpdatedDateTime=" + lastUpdatedDateTime +
                ", isArchived=" + isArchived +
                '}';
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Certificate that = (Certificate) o;
        return id == that.id && duration == that.duration && isArchived == that.isArchived && name.equals(that.name)
                && description.equals(that.description) && price.equals(that.price)
                && createdDateTime.equals(that.createdDateTime)
                && lastUpdatedDateTime.equals(that.lastUpdatedDateTime) && tags.equals(that.tags);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, duration, createdDateTime,
                lastUpdatedDateTime, isArchived);
    }

    @Override
    public int compareTo(Certificate o) {
        return Comparator.comparing(Certificate::getName)
                .thenComparing(Certificate::getCreatedDateTime)
                .compare(this, o);
    }
}
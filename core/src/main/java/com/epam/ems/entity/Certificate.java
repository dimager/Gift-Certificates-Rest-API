package com.epam.ems.entity;

import com.epam.ems.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "certificates")
@EntityListeners(AuditListener.class)
@NamedQueries({
        @NamedQuery(name = "Certificate.updateIsArchivedById", query = "update Certificate c set c.isArchived = true where c.id = :id"),
        @NamedQuery(name = "Certificate.findByIsArchivedFalse", query = "select c from Certificate c where c.isArchived = false"),
        @NamedQuery(name = "Certificate.findByIdEqualsAndIsArchivedFalse", query = "select c from Certificate c where c.id = :id and c.isArchived = false"),
        @NamedQuery(name = "Certificate.existsByIdEqualsAndIsArchivedFalse", query = "select (count(c) > 0) from Certificate c where c.id = :id and c.isArchived = false"),
        @NamedQuery(name = "Certificate.countBy", query = "select count(c) from Certificate c where c.isArchived = false"),
        @NamedQuery(name = "Certificate.findByTagsIn", query = "select c from Certificate c join c.tags t where t in :tags and c.isArchived = false group by c.id having count(c.id) = :amount"),
        @NamedQuery(name = "Certificate.countByTagsIn", query = "select c from Certificate c join c.tags t where t in :tags and c.isArchived = false group by c.id having count(c.id) = :amount")
})
public class Certificate extends BaseEntity implements Comparable<Certificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "certificate_id")
    @Setter
    @Getter
    private long id;

    @NotEmpty(message = "Name cant be empty")
    @Size(min = 1, max = 45, message = "Max name size is 45")
    @Setter
    @Getter
    @Column(length = 45)
    private String name;

    @NotEmpty(message = "Description cant be empty")
    @Size(min = 1, max = 255, message = "Max description size is 255")
    @Setter
    @Getter
    private String description;

    @NotNull(message = "Price should be present")
    @Setter
    @Getter
    @Positive(message = "Price must be positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Setter
    @Getter
    @Positive(message = "Duration must be positive")
    private short duration;

    @Setter
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "created_date_time", columnDefinition = "timestamp")
    private Timestamp createdDateTime;

    @Setter
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "last_update_date_time", columnDefinition = "timestamp")
    private Timestamp lastUpdatedDateTime;

    @Getter
    @Setter
    @ManyToMany
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JoinTable(name = "certificate_tags",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "certificate")
    @JsonIgnore
    private List<OrderCertificate> orderCertificates = new ArrayList<>();

    @Setter
    @Getter
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
                ", tags=" + tags +
                ", orderCertificates=" + orderCertificates +
                ", isArchived=" + isArchived +
                '}';
    }

    @Override
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
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, duration, createdDateTime, lastUpdatedDateTime, isArchived);
    }

    @Override
    public int compareTo(Certificate o) {
        return Comparator.comparing(Certificate::getName)
                .thenComparing(Certificate::getCreatedDateTime)
                .compare(this, o);
    }
}
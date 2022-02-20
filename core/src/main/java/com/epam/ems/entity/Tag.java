package com.epam.ems.entity;

import com.epam.ems.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "tags")
@NamedQueries({
        @NamedQuery(name = "Tag.findByIdEquals", query = "select t from Tag t where t.id = :id"),
        @NamedQuery(name = "Tag.existsByIdEquals", query = "select (count(t) > 0) from Tag t where t.id = :id"),
        @NamedQuery(name = "Tag.deleteByIdEquals", query = "delete from Tag t where t.id = :id"),
        @NamedQuery(name = "Tag.countBy", query = "select count(t) from Tag t"),
        @NamedQuery(name = "Tag.findAll", query = "select t from Tag t"),
        @NamedQuery(name = "Tag.findByNameEquals", query = "select t from Tag t where t.name = :name"),
        @NamedQuery(name = "Tag.existsByNameEquals", query = "select (count(t) > 0) from Tag t where t.name = :name")
})
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "tag_id")
    @Getter
    @Setter
    private long id;

    @NotEmpty(message = "Tag name should be not empty")
    @Size(min = 1, max = 255, message = "Wrong size of tag name")
    @Getter
    @Setter
    @Column(nullable = false, unique = true)

    private String name;
    @Getter
    @Setter
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Certificate> certificateList = new LinkedHashSet<>();

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tag tag = (Tag) o;
        return id == tag.id && name.equals(tag.name) && Objects.equals(certificateList, tag.certificateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name);
    }


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}

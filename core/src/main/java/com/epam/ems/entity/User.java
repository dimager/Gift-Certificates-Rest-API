package com.epam.ems.entity;

import com.epam.ems.listener.AuditListener;
import com.epam.ems.security.Role;
import com.epam.ems.security.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @Size(message = "Incorrect username length", min = 1, max = 45)
    @NotNull
    @Column(length = 45)
    private String username;


    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;

    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, orders);
    }


    @Override
    public List<Link> getLinks(String relation) {
        return super.getLinks(relation);
    }
}

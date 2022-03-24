package com.epam.ems.entity;


import com.epam.ems.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "orders")

public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "order_id")
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @Column(name = "puchase_date", nullable = false, columnDefinition = "timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp purchaseDate;


    @PositiveOrZero(message = "30108")
    @Getter
    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost = new BigDecimal("0");

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Setter
    @NotEmpty(message = "30106")
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderCertificate> orderCertificates = new ArrayList<>();


    public Order() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(purchaseDate, order.purchaseDate) && Objects.equals(cost, order.cost) && Objects.equals(user, order.user) && Objects.equals(orderCertificates, order.orderCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, purchaseDate, cost, user, orderCertificates);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", purchaseDate=" + purchaseDate +
                ", cost=" + cost +
                '}';
    }


}

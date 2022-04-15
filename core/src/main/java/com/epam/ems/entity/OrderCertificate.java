package com.epam.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_certificate")
@Data
public class OrderCertificate extends BaseEntity implements Serializable {
    @Id
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;
    @Positive(message = "30110")
    @Column(nullable = false)
    private long amount;

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public OrderCertificate() {
    }

    @PrePersist
    public void prePersist() {
        price = certificate.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderCertificate that = (OrderCertificate) o;
        return amount == that.amount && Objects.equals(order, that.order) && Objects.equals(certificate, that.certificate) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), order, certificate, amount, price);
    }
}

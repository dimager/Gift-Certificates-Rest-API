package com.epam.ems.dto;

import com.epam.ems.entity.OrderCertificate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class OrderDTO extends BaseDTO {
    @NotEmpty(message = "30106")
    private List<OrderCertificate> orderCertificates = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(orderCertificates, orderDTO.orderCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderCertificates);
    }
}

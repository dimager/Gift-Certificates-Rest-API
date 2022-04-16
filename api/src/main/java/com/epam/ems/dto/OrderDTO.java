package com.epam.ems.dto;

import com.epam.ems.entity.OrderCertificate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO extends BaseDTO {
    @NotEmpty(message = "30106")
    private List<OrderCertificate> orderCertificates = new ArrayList<>();

}

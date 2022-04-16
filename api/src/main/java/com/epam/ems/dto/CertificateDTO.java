package com.epam.ems.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class CertificateDTO extends BaseDTO {
    @NotEmpty(message = "30101")
    @Size(min = 1, max = 45, message = "30102")
    private String name;

    @NotEmpty(message = "30103")
    @Size(min = 1, max = 255, message = "30104")
    private String description;

    @NotNull(message = "30105")
    @Positive(message = "30106")
    private BigDecimal price;

    @Positive(message = "30107")
    private short duration;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Valid
    private Set<TagDTO> tags = new LinkedHashSet<>();
}

package com.epam.ems.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class TagDTO extends BaseDTO {
    @NotEmpty(message = "30111")
    @Size(min = 1, max = 255, message = "30112")
    private String name;

}

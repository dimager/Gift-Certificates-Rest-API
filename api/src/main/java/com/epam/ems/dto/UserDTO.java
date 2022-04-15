package com.epam.ems.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class UserDTO extends BaseDTO {
    private Long id;
    @NotNull
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username);
    }
}

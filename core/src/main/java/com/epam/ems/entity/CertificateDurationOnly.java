package com.epam.ems.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.util.Objects;

public class CertificateDurationOnly extends BaseEntity {
    @Setter
    @Getter
    @Positive(message = "Duration must be positive")
    private short duration;

    @Override
    public String toString() {
        return "CertificateDurationOnly{" +
                "duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CertificateDurationOnly that = (CertificateDurationOnly) o;
        return duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), duration);
    }
}
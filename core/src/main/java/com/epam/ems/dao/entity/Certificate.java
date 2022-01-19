package com.epam.ems.dao.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Certificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private short duration;
    private Timestamp createdTime;
    private Timestamp lastUpdatedTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return id == that.id && Double.compare(that.price, price) == 0 && duration == that.duration && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createdTime, that.createdTime) && Objects.equals(lastUpdatedTime, that.lastUpdatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createdTime, lastUpdatedTime);
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createdTime=" + createdTime +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }
}


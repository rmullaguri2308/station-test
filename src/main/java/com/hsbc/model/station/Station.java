package com.hsbc.model.station;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Station
 */
@Entity
@Table(name = "station", uniqueConstraints = @UniqueConstraint(name = "unique_name", columnNames = {"name"}))
public class Station implements Serializable {

    @Id
    long id;

    @Column(nullable = false)
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final Station other = (Station) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Station{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

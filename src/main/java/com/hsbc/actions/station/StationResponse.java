package com.hsbc.actions.station;

import java.util.Objects;

/**
 * Response DTO for {@code StationService}
 */
public class StationResponse {

    private String name;

    public StationResponse() {
    }

    public StationResponse(String name) {
        this.name = name;
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

        final StationResponse other = (StationResponse) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

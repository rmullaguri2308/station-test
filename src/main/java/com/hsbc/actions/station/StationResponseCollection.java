package com.hsbc.actions.station;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a collection of {@code StationResponse}
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StationResponseCollection {

    private Collection<StationResponse> stations;

    public StationResponseCollection() {
        this.stations = new ArrayList<>();
    }

    public StationResponseCollection(Collection<StationResponse> stations) {
        this.stations = new ArrayList<>();
        this.stations.addAll(stations);
    }

    public void addStations(Collection<StationResponse> stations) {
        this.stations.addAll(stations);
    }

    public Collection<StationResponse> getStations() {
        return stations;
    }

    public void setStations(Collection<StationResponse> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stations", stations)
                .toString();
    }
}

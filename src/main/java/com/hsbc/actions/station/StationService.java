package com.hsbc.actions.station;

import com.hsbc.infrastructure.exception.ResourceNotFoundException;
import com.hsbc.infrastructure.exception.ServiceUnavailableException;

/**
 * Service interface for station queries
 */
public interface StationService {

    /**
     * Retrieves stations for a specified name
     * @return {@code StationResponseCollection}
     */
    StationResponseCollection searchStation(String stationName) throws ResourceNotFoundException;

    /**
     * Retrieves all stations
     * @return {@code StationResponseCollection}
     */
    StationResponseCollection searchAll() throws ServiceUnavailableException;
}

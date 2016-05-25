package com.hsbc.actions.station;

import com.hsbc.infrastructure.exception.ResourceNotFoundException;
import com.hsbc.infrastructure.exception.ServiceUnavailableException;
import com.hsbc.infrastructure.repository.station.StationRepository;
import com.hsbc.model.station.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.hasText;

/**
 * Default implementation of {@code StationService}
 */
@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StationServiceImpl implements StationService {

    private StationRepository stationRepository;

    private HealthIndicator healthIndicator;

    @Override
    public StationResponseCollection searchStation(String stationName) throws ResourceNotFoundException {
        hasText(stationName, "Station name cannot be null or empty");

        Collection<Station> stations = stationRepository.findByNameStartingWith(stationName);

        if(stations.isEmpty())
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.toString());

        Collection<StationResponse> stationResponses = map(stations);

        return new StationResponseCollection(stationResponses);
    }

    @Override
    public StationResponseCollection searchAll() throws ServiceUnavailableException {
        if(!Status.UP.equals(healthIndicator.health().getStatus()))
            throw new ServiceUnavailableException(HttpStatus.SERVICE_UNAVAILABLE.toString());

        Collection<Station> stations = stationRepository.findAll();

        Collection<StationResponse> stationResponses = map(stations);

        return new StationResponseCollection(stationResponses);
    }

    private List<StationResponse> map(Collection<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getName())).collect(Collectors.toList());
    }

    @Autowired
    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Autowired
    @Qualifier("repositoryHealthIndicator")
    public void setHealthIndicator(HealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }
}

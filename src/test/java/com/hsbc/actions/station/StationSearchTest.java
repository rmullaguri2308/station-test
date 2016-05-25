package com.hsbc.actions.station;

import com.google.common.collect.ImmutableList;
import com.hsbc.infrastructure.exception.ResourceNotFoundException;
import com.hsbc.infrastructure.exception.ServiceUnavailableException;
import com.hsbc.infrastructure.repository.station.StationRepository;
import com.hsbc.model.station.Station;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;

import javax.servlet.UnavailableException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.when;

/**
 * Tests for
 */
@RunWith(MockitoJUnitRunner.class)
public class StationSearchTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private StationServiceImpl stationService = new StationServiceImpl();

    @Mock
    private StationRepository stationRepository;

    @Mock
    private HealthIndicator healthIndicator;

    @Before
    public void init() {
        stationService.setStationRepository(stationRepository);
    }

    @Test
    public void searchStation_shouldThrowExceptionForNullSearchCriteria() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Station name cannot be null or empty");

        stationService.searchStation(null);
    }

    @Test
    public void searchStation_shouldReturnLondonForInputLondon() {
        List<StationResponse> expectedStationResponses = ImmutableList.of(new StationResponse("LONDON"));
        List<Station> expectedStation = ImmutableList.of(new Station("LONDON"));

        when(stationRepository.findByNameStartingWith("LONDON")).thenReturn(expectedStation);

        StationResponseCollection stations = stationService.searchStation("LONDON");

        assertThat(stations, is(notNullValue()));
        assertThat(stations.getStations(), is(expectedStationResponses));
    }

    @Test
    public void searchStation_shouldThrowExceptionWhenStationNotFound() {
        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage(HttpStatus.NOT_FOUND.toString());

        List<Station> expectedStation = ImmutableList.of(new Station("LONDON"));
        when(stationRepository.findByNameStartingWith("LONDON")).thenReturn(expectedStation);

        stationService.searchStation("MILTON KEYNES");
    }

    @Test
    public void searchAll_shouldReturnAllStations() throws UnavailableException {
        List<StationResponse> expectedStationResponses = ImmutableList.of(new StationResponse("LONDON"),
                new StationResponse("DARTFORD"), new StationResponse("LIVERPOOL"));

        List<Station> expectedStations = ImmutableList.of(new Station("LONDON"), new Station("DARTFORD"),
                new Station("LIVERPOOL"));

        Health health = new Health.Builder(new Status("UP")).build();

        when(healthIndicator.health()).thenReturn(health);
        when(stationRepository.findAll()).thenReturn(expectedStations);

        StationResponseCollection stations = stationService.searchAll();

        assertThat(stations, is(notNullValue()));
        assertThat(stations.getStations(), is(expectedStationResponses));
    }

    @Test
    public void searchAll_shouldThrowExceptionWhenServiceIsDown() {
        expectedException.expect(ServiceUnavailableException.class);
        expectedException.expectMessage(HttpStatus.SERVICE_UNAVAILABLE.toString());

        List<Station> expectedStations = ImmutableList.of(new Station("LONDON"), new Station("DARTFORD"),
                new Station("LIVERPOOL"));

        Health health = new Health.Builder(new Status("DOWN")).build();

        when(healthIndicator.health()).thenReturn(health);
        when(stationRepository.findAll()).thenReturn(expectedStations);

        stationService.searchAll();
    }
}

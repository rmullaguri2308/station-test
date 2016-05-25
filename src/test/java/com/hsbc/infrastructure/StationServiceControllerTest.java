package com.hsbc.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hsbc.actions.station.StationResponse;
import com.hsbc.actions.station.StationResponseCollection;
import com.hsbc.actions.station.StationService;
import com.hsbc.infrastructure.exception.ResourceNotFoundException;
import com.hsbc.infrastructure.exception.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Tests for {@code StationServiceController}
 */
@RunWith(MockitoJUnitRunner.class)
public class StationServiceControllerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private StationServiceController stationServiceController = new StationServiceController();

    @Mock
    private StationService stationService;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        mockMvc = standaloneSetup(stationServiceController).build();
    }

    @Test
    public void searchStation_configurationTestForAValidName() throws Exception {
        Collection<StationResponse> stations = Lists.newArrayList(new StationResponse("London"));
        StationResponseCollection expected = new StationResponseCollection(stations);

        when(stationService.searchStation(anyString())).thenReturn(expected);

        mockMvc.perform(get("/station/{name}", "London").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(mapper.writeValueAsString(expected)));
    }

    @Test
    public void searchStation_configurationTestWithNoName() throws Exception {
        when(stationService.searchStation(anyString())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/station/{name}", "").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void searchStation_shouldReturnStationResponseCollectionForAValidStationName() {
        Collection<StationResponse> stations = Lists.newArrayList(new StationResponse("London Bridge"));
        StationResponseCollection expected = new StationResponseCollection(stations);

        when(stationService.searchStation(anyString())).thenReturn(expected);

        ResponseEntity responseEntity = stationServiceController.searchStation("London");
        assertThat(responseEntity, is(notNullValue()));
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(expected));
    }

    @Test
    public void searchAllStation_configurationTest() throws Exception {
        Collection<StationResponse> stations = Lists.newArrayList(new StationResponse("London"), new StationResponse("Kent"));
        StationResponseCollection expected = new StationResponseCollection(stations);

        when(stationService.searchAll()).thenReturn(expected);

        mockMvc.perform(get("/station/all").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(mapper.writeValueAsString(expected)));
    }

    @Test
    public void searchAllStation_configurationTestWhenServiceUnavailable() throws Exception {
        when(stationService.searchAll()).thenThrow(new ServiceUnavailableException());

        mockMvc.perform(get("/station/all").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isServiceUnavailable())
                .andExpect(status().reason("Service is temporarily unavailable"))
                .andExpect(content().string(""));
    }

    @Test
    public void searchAllStation_shouldReturnAllStation() {
        Collection<StationResponse> stations = Lists.newArrayList(new StationResponse("London Bridge"));
        StationResponseCollection expected = new StationResponseCollection(stations);

        when(stationService.searchAll()).thenReturn(expected);

        StationResponseCollection stationResponseCollection = stationServiceController.searchAllStations();
        assertThat(stationResponseCollection, is(notNullValue()));
        assertThat(stationResponseCollection, is(expected));
    }

    @Test
    public void searchAllStation_shouldThrowServiceUnavailableException() {
        expectedException.expect(ServiceUnavailableException.class);
        expectedException.expectMessage(HttpStatus.SERVICE_UNAVAILABLE.toString());

        when(stationService.searchAll()).thenThrow(new ServiceUnavailableException(HttpStatus.SERVICE_UNAVAILABLE.toString()));

        stationServiceController.searchAllStations();
    }

    @Test
    public void searchAllThatDoesNotExist_configurationTest() throws Exception {
        mockMvc.perform(get("/station/exist/false").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Api doesn't exist"))
                .andExpect(content().string(""));
    }
}

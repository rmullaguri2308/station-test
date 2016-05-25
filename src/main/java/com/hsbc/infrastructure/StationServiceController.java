package com.hsbc.infrastructure;

import com.hsbc.actions.station.StationResponseCollection;
import com.hsbc.actions.station.StationService;
import com.hsbc.infrastructure.exception.ResourceNotFoundException;
import com.hsbc.infrastructure.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handles search requests
 */
@Controller
@ComponentScan("com.hsbc")
@EnableAutoConfiguration
public class StationServiceController {

    private StationService stationService;

    @RequestMapping(value = "/station/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchStation(@PathVariable String name) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(stationService.searchStation(name));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }

    @RequestMapping(value = "/station/all", method = RequestMethod.GET)
    @ResponseBody
    public StationResponseCollection searchAllStations() throws ServiceUnavailableException {
        return stationService.searchAll();
    }

    @RequestMapping(value = "/station/exist/false", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Api doesn't exist")
    public void searchAllThatDoesNotExist() {

    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Service is temporarily unavailable")
    public void serviceUnavailableExceptionHandler(){

    }

    @Autowired
    public void setStationService(StationService stationService) {
        this.stationService = stationService;
    }
}

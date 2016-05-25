package com.hsbc.infrastructure;

import com.hsbc.actions.station.StationServiceImpl;
import com.hsbc.infrastructure.repository.station.RepositoryHealthIndicator;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Step definitions for {@code StationService}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest
public class StationSearchStepDefinition {

    private static final String URI = "http://localhost:8080";

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private List<String> allStations;

    private ResponseEntity<String> entity;

    private StationServiceImpl stationServiceImpl;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        stationServiceImpl = applicationContext.getBean("stationServiceImpl", StationServiceImpl.class);
    }

    @And("^the following train stations exist:$")
    public void theFollowingTrainStationsExist(DataTable dataTable) {
        allStations = dataTable.asList(String.class);

        allStations.stream().skip(1).forEach(
            name -> {
                ResponseEntity<String> entity = testRestTemplate.getForEntity(URI + "/station/" + name, String.class);
                assertThat(entity, is(notNullValue()));
                assertThat(entity.getStatusCode(), is(HttpStatus.OK));
                assertThat(entity.getBody(), containsString(name));
        });
    }

    @Given("^the service is up and running$")
    public void theServiceIsUpAndRunning() {
        stationServiceImpl.setHealthIndicator(new RepositoryHealthIndicator(Status.UP));

        ResponseEntity<String> entity = testRestTemplate.getForEntity(URI + "/health", String.class);
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getStatusCode(), is(HttpStatus.OK));
        assertThat(entity.getBody(), containsString("UP"));
    }

    @And("^I am on a RESTFull client screen$")
    public void iAmOnARESTFullClientScreen() {
        assertThat(true, is(true));
    }

    @When("^I enter the train station name \'([^\"]*)\'$")
    public void iEnterTheTrainStationNameDART(String name) {
        entity = testRestTemplate.getForEntity(URI + "/station/" + name, String.class);
    }

    @Then("^the response code should be (\\d+)$")
    public void theResponseCodeShouldBe(int arg0) {
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getStatusCode().value(), is(arg0));
    }

    @And("^the search should return a JSON response as following:(?:[:])?$")
    public void theSearchShouldReturnAJSONResponseAsFollowing(String jsonOutputAsString) {
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getBody().replaceAll("\\s", ""), is(jsonOutputAsString.replaceAll("\\s", "")));
    }

    @And("^the search should return a JSON response containing the \'([^\"]*)\' station$")
    public void theSearchShouldReturnAJSONResponseContainingTheStation(String name) {
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getBody(), containsString(name));
    }

    @When("^I input '([^\"]*)'$")
    public void theInputKINGSCROSS(String name) {
        entity = testRestTemplate.getForEntity(URI + "/station/" + name, String.class);
    }

    @Then("^the search should return no characters and no stations$")
    public void theSearchShouldReturnNoCharactersAndNoStations() {
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getBody(), isEmptyOrNullString());
    }

    @When("^I call the searchAll API$")
    public void iCallTheSearchAllAPI() {
        entity = testRestTemplate.getForEntity(URI + "/station/all", String.class);
    }

    @And("^the search should return all the stations available in a JSON format$")
    public void theSearchShouldReturnAllTheStationsAvailableInAJSONFormat() {
        assertThat(entity, is(notNullValue()));
        allStations.stream().skip(1).forEach(
            name -> {
                assertThat(entity, is(notNullValue()));
                assertThat(entity.getBody(), containsString(name));
        });
    }

    @Given("^the service is not up and running$")
    public void theServiceNotIsUpAndRunning() {
        stationServiceImpl.setHealthIndicator(new RepositoryHealthIndicator(Status.DOWN));
    }

    @When("^I call the searchAllThatDoesNotExist API$")
    public void iCallTheSearchAllThatDoesNotExistAPI() {
        entity = testRestTemplate.getForEntity(URI + "/station/exist/false", String.class);
    }

    @When("^I call the health API$")
    public void iCallTheHealthAPI() {
        entity = testRestTemplate.getForEntity(URI + "/health", String.class);
    }

    @And("^the response body should contain a JSON message telling that the service is \'([^\"]*)\'$")
    public void theResponseBodyShouldContainAJSONMessageTellingThatTheServiceIsUP(String status) {
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getStatusCode(), is(HttpStatus.OK));
        assertThat(entity.getBody(), containsString(status));
    }
}

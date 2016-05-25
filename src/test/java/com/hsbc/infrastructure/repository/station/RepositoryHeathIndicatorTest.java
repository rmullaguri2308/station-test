package com.hsbc.infrastructure.repository.station;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@code RepositoryHealthIndicator}
 */
@RunWith(Parameterized.class)
public class RepositoryHeathIndicatorTest {

    private final Status input;

    private final Status expected;

    private RepositoryHealthIndicator repositoryHealthIndicator;

    @Parameterized.Parameters
    public static Collection<Object[]> testData () {
        return Arrays.asList(new Object[][] {
                {Status.UP, Status.UP}, {Status.DOWN, Status.DOWN}, {Status.OUT_OF_SERVICE, Status.DOWN}, {Status.UNKNOWN, Status.DOWN}
        });
    }

    public RepositoryHeathIndicatorTest(Status input, Status expected) {
        this.input = input;
        this.expected = expected;
        this.repositoryHealthIndicator = new RepositoryHealthIndicator();
    }

    @Test
    public void health_shouldReturnHealthStatusUpByDefault() {
        Health health = repositoryHealthIndicator.health();

        assertThat(health, is(notNullValue()));
        assertThat(health.getStatus(), is(Status.UP));
    }

    @Test
    public void health_shouldMatchParameterizedTestData() {
        repositoryHealthIndicator = new RepositoryHealthIndicator(input);

        Health health = repositoryHealthIndicator.health();

        assertThat(health, is(notNullValue()));
        assertThat(health.getStatus(), is(expected));
    }
}

package com.hsbc.infrastructure.repository.station;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * A dummy repository health indicator
 *
 * TODO: Can be enhanced to handle domain specific health checks
 */
@Component
public class RepositoryHealthIndicator implements HealthIndicator {

    private Status healthStatus = Status.UP;

    public RepositoryHealthIndicator() {
    }

    public RepositoryHealthIndicator(Status healthStatus) {
        this.healthStatus = healthStatus;
    }

    @Bean
    @Override
    public Health health() {
        return healthStatus == Status.UP ?
                Health.up().build() :
                Health.down().withDetail("Error Code", HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}

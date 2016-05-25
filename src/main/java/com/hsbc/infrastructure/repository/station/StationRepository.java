package com.hsbc.infrastructure.repository.station;

import com.hsbc.model.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for {@code Station}
 */
@Transactional
public interface StationRepository extends JpaRepository<Station, String> {

    List<Station> findByNameStartingWith(String stationName);

}

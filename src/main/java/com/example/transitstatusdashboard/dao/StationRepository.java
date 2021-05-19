package com.example.transitstatusdashboard.dao;

import com.example.transitstatusdashboard.model.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StationRepository extends CrudRepository<Station, Integer> {

}

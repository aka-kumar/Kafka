package com.example.kafkaconsumer.jpa;

import com.example.kafkaconsumer.entity.Station;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<Station,Integer> {
}

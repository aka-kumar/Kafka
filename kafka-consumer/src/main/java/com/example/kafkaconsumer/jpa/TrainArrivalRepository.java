package com.example.kafkaconsumer.jpa;

import com.example.kafkaconsumer.entity.TrainArrival;
import org.springframework.data.repository.CrudRepository;

public interface TrainArrivalRepository extends CrudRepository<TrainArrival,Integer> {

}


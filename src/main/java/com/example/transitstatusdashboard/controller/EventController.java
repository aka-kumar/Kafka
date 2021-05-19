package com.example.transitstatusdashboard.controller;

import com.example.transitstatusdashboard.dao.StationRepository;
import com.example.transitstatusdashboard.model.Station;
import com.example.transitstatusdashboard.model.TrainArrival;
import com.example.transitstatusdashboard.model.TurnstileEvent;
import com.example.transitstatusdashboard.producer.ProducerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class EventController {

    @Autowired
    ProducerConfig producerConfig;

    @Autowired
    StationRepository stationRepository;

    @PostMapping("/trainarrival")
    public ResponseEntity<TrainArrival> postTrainArrival(@RequestBody @Valid TrainArrival trainArrival) throws JsonProcessingException, InterruptedException {

        producerConfig.sendTrainArrival(trainArrival);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainArrival);
    }

    //PUT
    @PutMapping("/trainarrival")
    public ResponseEntity<?> putTrainArrival(@RequestBody @Valid TrainArrival trainArrival) throws JsonProcessingException, InterruptedException {


        if(trainArrival.getStation_id()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the Station_id");
        }

        producerConfig.sendTrainArrival(trainArrival);
        return ResponseEntity.status(HttpStatus.OK).body(trainArrival);
    }

    @PostMapping("/turnstile")
    public ResponseEntity<TurnstileEvent> postTurnstileEvent(@RequestBody @Valid TurnstileEvent turnstileEvent) throws JsonProcessingException, InterruptedException {

        producerConfig.sendTurnstileEvent(turnstileEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(turnstileEvent);
    }

    //PUT
    @PutMapping("/turnstile")
    public ResponseEntity<?> putTurnstileEvent(@RequestBody @Valid TurnstileEvent turnstileEvent) throws JsonProcessingException, InterruptedException {


        if(turnstileEvent.getStation_id()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the Station_id");
        }

        producerConfig.sendTurnstileEvent(turnstileEvent);
        return ResponseEntity.status(HttpStatus.OK).body(turnstileEvent);
    }

//    @GetMapping("/stationdata")
//    public @ResponseBody Iterable<Station>  findStationData(){
//        // This returns a JSON
//        return stationRepository.findAll();
//    }
}

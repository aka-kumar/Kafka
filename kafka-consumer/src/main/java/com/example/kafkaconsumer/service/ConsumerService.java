package com.example.kafkaconsumer.service;

import com.example.kafkaconsumer.entity.Station;
import com.example.kafkaconsumer.entity.TrainArrival;
import com.example.kafkaconsumer.entity.TurnstileEvent;
import com.example.kafkaconsumer.jpa.StationRepository;
import com.example.kafkaconsumer.jpa.TrainArrivalRepository;
import com.example.kafkaconsumer.jpa.TurnstileEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    private TrainArrivalRepository trainArrivalRepository;

    @Autowired
    private TurnstileEventRepository turnstileEventRepository;

    @Autowired
    private StationRepository stationRepository;

    public void processTrainArrival(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        TrainArrival trainArrival = objectMapper.readValue(consumerRecord.value(), TrainArrival.class);
        log.info("trainArrival : {} ", trainArrival);

        if(trainArrival.getStation_id()!=null && trainArrival.getStation_id()==000){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }
                save(trainArrival);

    }

    public void processTurnstileEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        TurnstileEvent turnstileEvent = objectMapper.readValue(consumerRecord.value(), TurnstileEvent.class);
        log.info("turnstileEvent : {} ", turnstileEvent);

        if(turnstileEvent.getStation_id()!=null && turnstileEvent.getStation_id()==000){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }
        saveTurnstile(turnstileEvent);

    }

    public void processStationData(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        Station station = objectMapper.readValue(consumerRecord.value(), Station.class);
        log.info("station : {} ", station);

        if(station.getStation_id()!=null && station.getStation_id()==000){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }
        saveStationData(station);

    }

    private void save(TrainArrival trainArrival) {
        trainArrivalRepository.save(trainArrival);
        log.info("Successfully Persisted the Train Event {} ", trainArrival);
    }

    private void saveTurnstile(TurnstileEvent turnstileEvent) {
        turnstileEventRepository.save(turnstileEvent);
        log.info("Successfully Persisted the Turnstile Event {} ", turnstileEvent);
    }

    private void saveStationData(Station station) {
        stationRepository.save(station);
        log.info("Successfully Persisted the Station Event {} ", station);
    }

    public void handleRecovery(ConsumerRecord<Integer,String> record){

        Integer key = record.key();
        String message = record.value();

        ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(key, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, message, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, message, result);
            }
        });
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}

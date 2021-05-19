package com.example.transitstatusdashboard.producer;

import com.example.transitstatusdashboard.model.Station;
import com.example.transitstatusdashboard.model.TrainArrival;
import com.example.transitstatusdashboard.model.TurnstileEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ProducerConfig {

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    String topic = "org.station.arrivals";
    String topic1 = "org.station.turnstiles";
    String topic2 = "org.station.stations";

    @Autowired
    ObjectMapper objectMapper;

//    public void sendTrainArrival(TrainArrival trainArrival) throws JsonProcessingException {
//
//        Integer key = trainArrival.getStation_id();
//        String value = objectMapper.writeValueAsString(trainArrival);
//
//        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.sendDefault(key,value);
//        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                handleFailure(key, value, ex);
//            }
//
//            @Override
//            public void onSuccess(SendResult<Integer, String> result) {
//                handleSuccess(key, value, result);
//            }
//        });
//    }

    public ListenableFuture<SendResult<Integer,String>> sendTrainArrival(TrainArrival trainArrival) throws JsonProcessingException {

        Integer key = trainArrival.getStation_id();
        String value = objectMapper.writeValueAsString(trainArrival);

        ProducerRecord<Integer,String> producerRecord = buildProducerRecord(key, value, topic);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

//    public SendResult<Integer, String> sendTrainArrivalSynchronous(TrainArrival trainArrival) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
//
//        Integer key = trainArrival.getStation_id();
//        String value = objectMapper.writeValueAsString(trainArrival);
//        SendResult<Integer,String> sendResult=null;
//        try {
//            sendResult = kafkaTemplate.sendDefault(key,value).get(1, TimeUnit.SECONDS);
//        } catch (ExecutionException | InterruptedException e) {
//            log.error("ExecutionException/InterruptedException Sending the Message and the exception is {}", e.getMessage());
//            throw e;
//        } catch (Exception e) {
//            log.error("Exception Sending the Message and the exception is {}", e.getMessage());
//            throw e;
//        }
//
//        return sendResult;
//
//    }

    public ListenableFuture<SendResult<Integer,String>> sendTurnstileEvent(TurnstileEvent turnstileEvent) throws JsonProcessingException {

        Integer key = turnstileEvent.getStation_id();
        String value = objectMapper.writeValueAsString(turnstileEvent);

        ProducerRecord<Integer,String> producerRecord = buildProducerTurnstileRecord(key, value, topic1);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerTurnstileRecord(Integer key, String value, String topic1) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic1, null, key, value, recordHeaders);
    }

    public ListenableFuture<SendResult<Integer,String>> sendStationData(Station station) throws JsonProcessingException {

        Integer key = station.getStation_id();
        String value = objectMapper.writeValueAsString(station);

        ProducerRecord<Integer,String> producerRecord = buildProducerStationRecord(key, value, topic2);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerStationRecord(Integer key, String value, String topic2) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic2, null, key, value, recordHeaders);
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

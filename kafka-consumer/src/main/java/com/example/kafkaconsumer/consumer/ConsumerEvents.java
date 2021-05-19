package com.example.kafkaconsumer.consumer;

import com.example.kafkaconsumer.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerEvents {

    @Autowired
    private ConsumerService consumerService;

    @KafkaListener(topics = {"org.station.arrivals"})
  //  @KafkaListener(topics = {"org.station.turnstiles"})
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord : {} ", consumerRecord );
        consumerService.processTrainArrival(consumerRecord);
    //    consumerService.processTurnstileEvent(consumerRecord);

    }

    @KafkaListener(topics = {"org.station.turnstiles"})
    public void onMessage1(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord : {} ", consumerRecord );
        consumerService.processTurnstileEvent(consumerRecord);

    }

    @KafkaListener(topics = {"org.station.stations"})
    public void onMessage2(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord : {} ", consumerRecord );
        consumerService.processStationData(consumerRecord);

    }

}

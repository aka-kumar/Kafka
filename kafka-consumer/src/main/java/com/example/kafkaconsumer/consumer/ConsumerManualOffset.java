package com.example.kafkaconsumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public class ConsumerManualOffset implements AcknowledgingMessageListener<Integer,String> {

    @Override
    @KafkaListener(topics = {"org.station.arrivals"})
    @KafkaListener(topics = {"org.station.turnstiles"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("ConsumerRecord : {} ", consumerRecord );
        acknowledgment.acknowledge();
    }

//    @Override
//    @KafkaListener(topics = {"org.station.turnstiles"})
//    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("ConsumerRecord : {} ", consumerRecord );
//        acknowledgment.acknowledge();
//    }
}

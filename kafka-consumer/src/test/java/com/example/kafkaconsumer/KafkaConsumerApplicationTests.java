package com.example.kafkaconsumer;

import com.example.kafkaconsumer.consumer.ConsumerEvents;
import com.example.kafkaconsumer.entity.TrainArrival;
import com.example.kafkaconsumer.jpa.TrainArrivalRepository;
import com.example.kafkaconsumer.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(topics = {"org.station.arrivals"}, partitions = 1)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
		, "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
class KafkaConsumerApplicationTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	KafkaListenerEndpointRegistry endpointRegistry;

	@SpyBean
	ConsumerEvents consumerEvents;

	@SpyBean
	ConsumerService consumerService;

	@Autowired
	TrainArrivalRepository trainArrivalRepository;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {

		for (MessageListenerContainer messageListenerContainer : endpointRegistry.getListenerContainers()){
			ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
		}
	}

	@AfterEach
	void tearDown() {
		trainArrivalRepository.deleteAll();
	}

	@Test
	void testTrainArrival() throws ExecutionException, InterruptedException, JsonProcessingException {
		//given
		String json = " {\"station_id\":40020,\"train_id\":12296,\"direction\":\"E\",\"line\":\"blue\",\"train_status\":\"onTime\",\"prev_station_id\":40010,\"prev_direction\":\"W\"}";
		kafkaTemplate.sendDefault(json).get();

		//when
		CountDownLatch latch = new CountDownLatch(1);
		latch.await(3, TimeUnit.SECONDS);

		//then
		verify(consumerEvents, times(1)).onMessage(isA(ConsumerRecord.class));
		verify(consumerService, times(1)).processTrainArrival(isA(ConsumerRecord.class));

		List<TrainArrival> trainArrivalList = (List<TrainArrival>) trainArrivalRepository.findAll();
		assert trainArrivalList.size() ==1;
		trainArrivalList.forEach(trainArrival -> {
			assert trainArrival.getStation_id()!=null;
			assertEquals(40020, trainArrival.getStation_id());
		});

	}

//	@Test
//	void contextLoads() {
//	}

}

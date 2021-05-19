package com.example.transitstatusdashboard.config;

import lombok.Value;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AutoCreateConfig {

  //  @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic trainArrivals(){
        return TopicBuilder.name("org.station.arrivals")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic turnstileEvents(){
        return TopicBuilder.name("org.station.turnstiles")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

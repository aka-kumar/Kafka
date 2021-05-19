package com.example.transitstatusdashboard;

import com.example.transitstatusdashboard.config.AppConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class TransitStatusDashboardApplication {
	private static final Logger logger = LogManager.getLogger();
	public static void main(String[] args) {

		SpringApplication.run(TransitStatusDashboardApplication.class, args);

		final Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
		props.put(StreamsConfig.STATE_DIR_CONFIG, AppConfigs.stateStoreLocation);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		StreamsBuilder streamsBuilder = new StreamsBuilder();

//		Creating Turnstiles KTable using Kafka Streams
		KTable<String, String> Turnstiles = streamsBuilder.table(AppConfigs.topicName);
		Turnstiles.toStream().print(Printed.<String, String>toSysOut().withLabel("Turnstiles"));

//		Creating Turnstiles_Summary KTable using Kafka Streams
		KTable<String, String> Turnstiles_Summary = Turnstiles.filter((k, v) -> k.matches(AppConfigs.regExSymbol) && !v.isEmpty());
		Turnstiles_Summary.toStream().print(Printed.<String, String>toSysOut().withLabel("Turnstiles_Summary"));

		KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);

		streams.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Shutting down servers");
			streams.close();
		}));


	}

}

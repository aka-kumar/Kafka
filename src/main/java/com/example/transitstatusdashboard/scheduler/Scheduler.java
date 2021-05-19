package com.example.transitstatusdashboard.scheduler;

import com.example.transitstatusdashboard.dao.StationRepository;
import com.example.transitstatusdashboard.model.Station;
import com.example.transitstatusdashboard.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    ProducerConfig producerConfig;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void cronJobSch() throws Exception {
        System.out.println("Java Cron job for Data Syncing started.");

        List<Station> stations = (List<Station>) stationRepository.findAll();
        for (Station station: stations) {
            producerConfig.sendStationData(station);
        }

        System.out.println("Java Cron job for Data Syncing finished successfully.");
    }
}

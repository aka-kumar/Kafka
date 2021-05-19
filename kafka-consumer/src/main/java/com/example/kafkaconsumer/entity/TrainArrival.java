package com.example.kafkaconsumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class TrainArrival {
    @Id
    private Integer station_id;
    private Integer train_id;
    private String direction;
    private String line;
    private String train_status;
    private Integer prev_station_id;
    private String prev_direction;
}

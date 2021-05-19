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
public class Station {

    @Id
    private Integer stop_id;

    private String direction_id;

    private String stop_name;

    private String station_name;

    private String station_descriptive_name;

    private Integer station_id;

    private Integer order;

    private String red;

    private String green;

    private String blue;
}

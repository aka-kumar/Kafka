package com.example.transitstatusdashboard.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "stations"
)
@Data
public class Station {

    @Id
    @Column(
            name = "stop_id"
    )
    private Integer stop_id;

    @Column(
            name = "direction_id"
    )
    private String direction_id;

    @Column(
            name = "stop_name"
    )
    private String stop_name;

    @Column(
            name = "station_name"
    )
    private String station_name;

    @Column(
            name = "station_descriptive_name"
    )
    private String station_descriptive_name;

    @Column(
            name = "station_id"
    )
    private Integer station_id;

    @Column(
            name = "order"
    )
    private Integer order;

    @Column(
            name = "red"
    )
    private String red;

    @Column(
            name = "green"
    )
    private String green;

    @Column(
            name = "blue"
    )
    private String blue;
}

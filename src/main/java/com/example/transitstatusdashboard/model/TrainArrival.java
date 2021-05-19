package com.example.transitstatusdashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TrainArrival {

    private Integer station_id;
    private Integer train_id;
    private String direction;
    private String line;
    private String train_status;
    private Integer prev_station_id;
    private String prev_direction;
}

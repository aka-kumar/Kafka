package com.example.transitstatusdashboard;

import com.example.transitstatusdashboard.model.TrainArrival;

public class MockUtils {

    public static final Integer STATION_ID = 40020;
    public static final Integer TRAIN_ID = 12296;
    public static final String DIRECTION = "E";
    public static final String LINE = "blue";
    public static final String TRAIN_STATUS = "onTime";
    public static final Integer PREV_STATION_ID = 40010;
    public static final String PREV_DIRECTION = "W";

    private MockUtils() {
    }

    public static TrainArrival getMockTrainArrival() {
        return TrainArrival.builder()
                .station_id(STATION_ID)
                .train_id(TRAIN_ID)
                .direction(DIRECTION)
                .line(LINE)
                .train_status(TRAIN_STATUS)
                .prev_station_id(PREV_STATION_ID)
                .prev_direction(PREV_DIRECTION)
                .build();
    }

}

package com.example.transitstatusdashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TurnstileEvent {

    private Integer station_id;
    private String station_name;
    private String line;
}

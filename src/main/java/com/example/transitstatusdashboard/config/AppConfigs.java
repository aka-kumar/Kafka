package com.example.transitstatusdashboard.config;

public class AppConfigs {

    public final static String applicationID = "StreamingTable";
    public final static String bootstrapServers = "localhost:9092";
    public final static String topicName = "org.station.turnstiles";
    public final static String stateStoreLocation = "tmp/state-store";
    public final static String regExSymbol = "(?i)station_id";
}

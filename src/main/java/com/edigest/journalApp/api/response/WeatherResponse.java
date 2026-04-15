package com.edigest.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse{
    private Location location;
    private Current current;

    @Getter
    @Setter
    public static class Condition{
        private String text;
        private String icon;
        private int code;
    }

    @Getter
    @Setter
    public static class Current{
        private int last_updated_epoch;
        private String last_updated;
        private double temp_c;
        private double temp_f;
        private int is_day;
        private Condition condition;
        private double wind_mph;
        private double wind_kph;
        private int wind_degree;
        private String wind_dir;
        private int pressure_mb;
        private double pressure_in;
        private int precip_mm;
        private int precip_in;
        private int humidity;
        private int cloud;
        private double feelslike_c;
        private double feelslike_f; // Changed to double to match JSON
        private double windchill_c;
        private double windchill_f;
        private double heatindex_c;
        private double heatindex_f; // Changed to double to match JSON
        private double dewpoint_c;
        private double dewpoint_f;
        private int vis_km;
        private int vis_miles;
        private int uv;
        private double gust_mph;
        private double gust_kph;
        private double short_rad;
        private double diff_rad;
        private double dni;
        private double gti;
    }

    @Getter
    @Setter
    public static class Location{
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        private String tz_id;
        private int localtime_epoch;
        private String localtime;
    }
}


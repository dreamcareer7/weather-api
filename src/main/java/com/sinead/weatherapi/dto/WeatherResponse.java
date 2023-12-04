package com.sinead.weatherapi.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class WeatherResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private String date;
    private JsonNode average_weather_info;
    private JsonNode hourly_units;
    private JsonNode hourly;

    // Constructors, getters, and setters below
    public WeatherResponse() {}

    public WeatherResponse(double latitude, double longitude, String timezone, String date, JsonNode average_weather_info, JsonNode hourly_units, JsonNode hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.date = date;
        this.average_weather_info = average_weather_info;
        this.hourly_units = hourly_units;
        this.hourly = hourly;
    }

    // Getters and setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JsonNode getAverage_weather_info() {
        return average_weather_info;
    }

    public void setAverage_weather_info(JsonNode average_weather_info) {
        this.average_weather_info = average_weather_info;
    }

    public JsonNode getHourly_units() {
        return hourly_units;
    }

    public void setHourly_units(JsonNode hourly_units) {
        this.hourly_units = hourly_units;
    }

    public JsonNode getHourly() {
        return hourly;
    }

    public void setHourly(JsonNode hourly) {
        this.hourly = hourly;
    }
}

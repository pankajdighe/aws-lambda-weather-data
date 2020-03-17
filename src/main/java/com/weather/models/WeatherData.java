package com.weather.models;

/**
 * Created by pankaj on 3/15/20.
 */
public class WeatherData {

    private Location location;
    private Double temperature=Double.MAX_VALUE;
    private Double feels_like = Double.MAX_VALUE;
    private Double temperature_max=Double.MAX_VALUE;
    private Integer pressure=Integer.MAX_VALUE;
    private Integer humidity=Integer.MAX_VALUE;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(Double feels_like) {
        this.feels_like = feels_like;
    }

    public Double getTemperature_max() {
        return temperature_max;
    }

    public void setTemperature_max(Double temperature_max) {
        this.temperature_max = temperature_max;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer Integer) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }



}

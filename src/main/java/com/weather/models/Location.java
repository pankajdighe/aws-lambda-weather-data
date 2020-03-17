package com.weather.models;

/**
 * Created by pankaj on 3/15/20.
 */
public class Location {

    private String locationName="San Jose";
    private String country="US";
    private Double latitude=Double.MAX_VALUE;
    private Double longitude=Double.MAX_VALUE;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


}

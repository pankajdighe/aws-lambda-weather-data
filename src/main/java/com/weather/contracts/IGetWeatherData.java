package com.weather.contracts;

import com.weather.models.WeatherData;

import java.util.Map;

/**
 * Created by pankaj on 3/15/20.
 */
public interface IGetWeatherData {

    void setParser(IWeatherDataParser parser);
    WeatherData getData(String location);
}

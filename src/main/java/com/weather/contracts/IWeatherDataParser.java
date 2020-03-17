package com.weather.contracts;

import com.weather.models.WeatherData;

import javax.ws.rs.core.Response;

/**
 * Created by pankaj on 3/15/20.
 */
public interface IWeatherDataParser {
    WeatherData parse(Response response);
}

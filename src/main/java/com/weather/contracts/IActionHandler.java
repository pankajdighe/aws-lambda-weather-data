package com.weather.contracts;

import com.weather.models.WeatherData;

/**
 * Created by pankaj on 3/15/20.
 */
public interface IActionHandler {
    void setNextHandler(IActionHandler nextHandler);
    void handle(WeatherData weatherData);
    void handle(String weatherData);
}

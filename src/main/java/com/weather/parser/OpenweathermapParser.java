package com.weather.parser;

import com.weather.contracts.IWeatherDataParser;
import com.weather.models.Location;
import com.weather.models.WeatherData;
import com.weather.models.openweathermap.WeatherWrapper;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

/**
 * Created by pankaj on 3/15/20.
 */
public class OpenweathermapParser implements IWeatherDataParser {

    public WeatherData parse(Response response) {
        WeatherData weatherData = null;

        WeatherWrapper weatherWrapper = parseResponse(response);
        weatherData = convertWeatherResponse(weatherWrapper);
        return weatherData;
    }

    private WeatherWrapper parseResponse(Response response) {
        WeatherWrapper weatherWrapper = null;

        if(response.getStatusInfo().getStatusCode()==Response.Status.OK.getStatusCode()) {
            weatherWrapper = response.readEntity(WeatherWrapper.class);
        }
        return weatherWrapper;
    }

    private WeatherData convertWeatherResponse(WeatherWrapper weatherWrapper) {
        WeatherData weatherData = null;
        if(weatherWrapper!=null) {
            weatherData = new WeatherData();
            weatherData.setFeels_like(weatherWrapper.getMain().getFeelsLike()==null? -200: weatherWrapper.getMain().getFeelsLike());
            weatherData.setHumidity(weatherWrapper.getMain().getHumidity()==null ? -200: weatherWrapper.getMain().getHumidity());
            weatherData.setPressure(weatherWrapper.getMain().getPressure()==null ? -200: weatherWrapper.getMain().getPressure());
            weatherData.setTemperature(weatherWrapper.getMain().getTemp()==null ?-200: weatherWrapper.getMain().getTemp());
            weatherData.setTemperature_max(weatherWrapper.getMain().getTempMax()==null ? -200: weatherWrapper.getMain().getTempMax());

            Location location = new Location();
            location.setLocationName(weatherWrapper.getName());
            location.setCountry(weatherWrapper.getSys().getCountry());
            location.setLatitude(weatherWrapper.getCoord().getLat());
            location.setLongitude(weatherWrapper.getCoord().getLon());

            weatherData.setLocation(location);
        }
        return weatherData;
    }
}

package com.weather.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.models.WeatherData;

import java.io.IOException;

/**
 * Created by pankaj on 3/15/20.
 */
public class ConvertObjectToString {

    public static String convertObjectToJson(WeatherData weatherData) {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr="";

        try {

            // get Oraganisation object as a json string
             jsonStr = Obj.writeValueAsString(weatherData);

            // Displaying JSON String
            System.out.println(jsonStr);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }
}

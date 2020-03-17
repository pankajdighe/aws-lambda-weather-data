package com.weather.driver;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.weather.contracts.IActionHandler;
import com.weather.contracts.IGetWeatherData;
import com.weather.handlers.StoreDataInDynamoDb;
import com.weather.handlers.StoreDataInS3Bucket;
import com.weather.handlers.StoreDataInSQS;
import com.weather.integration.OpenweathermapIntegration;
import com.weather.models.WeatherData;
import com.weather.parser.OpenweathermapParser;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 2/13/20.
 */
public class ProducerDriver implements RequestHandler<Map<String,Object> , String> {
    final static Logger logger = Logger.getLogger(ProducerDriver.class);
    private static final String OK = "ok";

    public String handleRequest(Map<String,Object> input, Context context) {
        logger.debug("Processing started");

        IGetWeatherData weatherDataApi = new OpenweathermapIntegration();
        weatherDataApi.setParser(new OpenweathermapParser());

        if (input.containsKey("location")) {
            String location = input.get("location").toString();
            logger.debug("Location received "+location);
            WeatherData weatherData=weatherDataApi.getData(location);
            if(weatherData!=null) {
                IActionHandler actionHandler = getActionHandlerChain();
                actionHandler.handle(weatherData);
            }
        }
        logger.debug("Processing finished");
        return OK;
    }


    private IActionHandler getActionHandlerChain() {
        IActionHandler s3Handle= new StoreDataInS3Bucket("weather-data-integration");
        IActionHandler dynamoDbHandle= new StoreDataInDynamoDb();
        IActionHandler sqsHandle= new StoreDataInSQS();
        s3Handle.setNextHandler(dynamoDbHandle);
        dynamoDbHandle.setNextHandler(sqsHandle);
        return s3Handle;
    }

    public static void main(String[] args) {
        ProducerDriver driver = new ProducerDriver();
        Map<String,Object> input = new HashMap();
        input.put("location", "Pune");
        driver.handleRequest(input, null);

    }
}

package com.weather.driver;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
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
public class ConsumerDriver implements RequestHandler<SQSEvent, String>{
    final static Logger logger = Logger.getLogger(ConsumerDriver.class);
    private static final String OK = "ok";

    public String handleRequest(SQSEvent event, Context context) {
        logger.debug("Processing started");
        for(SQSEvent.SQSMessage msg : event.getRecords()){
            logger.debug(new String(msg.getBody()));
            IActionHandler actionHandler = getActionHandlerChain();
            actionHandler.handle(new String(msg.getBody()));
        }
        logger.debug("Processing finished");
        return OK;
    }


    private IActionHandler getActionHandlerChain() {
        IActionHandler s3Handle= new StoreDataInS3Bucket("weather-data-integration-poll");
        return s3Handle;
    }

}

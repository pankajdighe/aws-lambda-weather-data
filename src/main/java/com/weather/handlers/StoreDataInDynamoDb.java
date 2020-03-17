package com.weather.handlers;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.weather.contracts.IActionHandler;
import com.weather.factory.AWSClientFactory;
import com.weather.models.WeatherData;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by pankaj on 3/15/20.
 */
public class StoreDataInDynamoDb implements IActionHandler {
    IActionHandler nextHandler;
    final static String DYNAMO_TABLE="weather_data";
    final static Logger logger = Logger.getLogger(StoreDataInDynamoDb.class);
    public void setNextHandler(IActionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handle(WeatherData weatherData) {
        try {
            logger.debug("storing the data in dynamodb");
            storeData(weatherData);
        } catch (Exception e) {
            logger.debug("Exception occurred while storing the data");
            e.printStackTrace();
        } finally {
            if(nextHandler!=null) {
                nextHandler.handle(weatherData);
            }
        }
    }

    public void handle(String weatherData) {
        logger.debug("empty implementation");
    }

    private void storeData(WeatherData weatherData) {
        AWSClientFactory awsClientFactory = new AWSClientFactory(Regions.US_EAST_2);
        AmazonDynamoDB dynamoDbClient = awsClientFactory.getDynamoDbClient();
        DynamoDB dynamoDb = new DynamoDB(dynamoDbClient);
        dynamoDb.getTable(DYNAMO_TABLE)
                .putItem(
                        new PutItemSpec().withItem(new Item()
                                .withString("id", UUID.randomUUID().toString())
                                .withString("location_name", weatherData.getLocation().getLocationName())
                                 .withDouble("temperature", weatherData.getTemperature())
                                 .withDouble("temperature_max", weatherData.getTemperature_max())
                                 .withInt("pressure", weatherData.getPressure())
                                 .withString("country", weatherData.getLocation().getCountry())
                        )
                );
    }
}

package com.weather.handlers;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.weather.contracts.IActionHandler;
import com.weather.factory.AWSClientFactory;
import com.weather.models.WeatherData;
import com.weather.util.ConvertObjectToString;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by pankaj on 3/15/20.
 */
public class StoreDataInS3Bucket implements IActionHandler {

    IActionHandler nextHandler;

    private String s3BucketName = "weather-data-integration";
    private static final String FILE_PREFIX = "weather-";
    final static Logger logger = Logger.getLogger(StoreDataInS3Bucket.class);

    public void setNextHandler(IActionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public StoreDataInS3Bucket(String s3BucketName) {
        this.s3BucketName=s3BucketName;
    }


    public void handle(WeatherData weatherData) {
        try {
            logger.debug("storing the data in s3 bucket");
            handle(ConvertObjectToString.convertObjectToJson(weatherData));

        } catch (Exception e) {
            logger.debug("storing the data in s3 bucket");
        } finally {
            if(nextHandler!=null) {
                nextHandler.handle(weatherData);
            }
        }
    }

    public void handle(String data) {
        try {
            logger.debug("storing the data in s3 bucket");
            uploadToS3(data);
        } catch (Exception e) {
            logger.debug("storing the data in s3 bucket");
        } finally {
            if(nextHandler!=null) {
                nextHandler.handle(data);
            }
        }
    }


    private void uploadToS3(String weatherData)  {

        try {
            AWSClientFactory awsClientFactory = new AWSClientFactory(Regions.US_EAST_2);
            AmazonS3 s3client = awsClientFactory.getS3client();

            s3client.putObject(
                    s3BucketName,
                    FILE_PREFIX+"_"+UUID.randomUUID().toString(),
                    weatherData
            );
        } catch (SdkClientException e) {
            logger.debug("Exeption occurred while storing the data to S3");
            e.printStackTrace();
        }


    }
}

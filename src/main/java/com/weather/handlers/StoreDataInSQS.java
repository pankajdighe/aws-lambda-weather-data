package com.weather.handlers;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.weather.contracts.IActionHandler;
import com.weather.factory.AWSClientFactory;
import com.weather.models.WeatherData;
import com.weather.util.ConvertObjectToString;
import org.apache.log4j.Logger;

/**
 * Created by pankaj on 3/15/20.
 */
public class StoreDataInSQS implements IActionHandler {

    IActionHandler nextHandler;
    final static String SQS_QUEUE_NAME="WEATHER_PIPE";
    final static Logger logger = Logger.getLogger(StoreDataInSQS.class);

    public void setNextHandler(IActionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handle(WeatherData weatherData) {
        try {
            logger.debug("Sending the data to SQS");

            sendMessageToQueue(ConvertObjectToString.convertObjectToJson(weatherData));

        } catch (Exception e) {

        } finally {
            if(nextHandler!=null) {
                nextHandler.handle(weatherData);
            }
        }
    }

    private void sendMessageToQueue(String message) {
        try {
            AWSClientFactory awsClientFactory = new AWSClientFactory(Regions.US_EAST_2);

            AmazonSQS sqs = awsClientFactory.getSqs();

            CreateQueueRequest createQueueRequest = new CreateQueueRequest(SQS_QUEUE_NAME);
            String myQueueURL = sqs.createQueue(createQueueRequest).getQueueUrl();

            logger.debug("Sending msg '" + message + "' to Q: " + myQueueURL);

            SendMessageResult smr = sqs.sendMessage(new SendMessageRequest()
                    .withQueueUrl(myQueueURL)
                    .withMessageBody(message));
            logger.debug("SendMessage succeeded with messageId " + smr.getMessageId()
                    + ", sequence number " + smr.getSequenceNumber() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(String weatherData) {
        logger.debug("empty implementation");
    }
}

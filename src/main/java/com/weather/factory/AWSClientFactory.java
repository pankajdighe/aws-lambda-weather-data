package com.weather.factory;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * Created by pankaj on 3/15/20.
 */
public class AWSClientFactory {
    private Regions region = Regions.US_EAST_2;

    public AWSClientFactory(Regions region) {
        this.region=region;
    }

    public AmazonS3 getS3client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }

    public AmazonDynamoDB getDynamoDbClient() {
       return AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .build();
    }

    public AmazonSQS getSqs() {
        return AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .build();
    }

}

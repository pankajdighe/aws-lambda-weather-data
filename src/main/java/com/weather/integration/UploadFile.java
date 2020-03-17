package com.weather.integration;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

/**
 * Created by pankaj on 2/13/20.
 */
public class UploadFile implements RequestHandler<Map<String,Object> , String> {


    private static final String S3_BUCKET_NAME = "integration-bucket-1190";
    private static final String OK = "ok";
    private static final String FILE_PREFIX = "integration-";

    private void uploadToS3(String bucketName, String name, String input)  {

        try {
            AmazonS3 s3client = AmazonS3ClientBuilder
                    .standard()
                    .withRegion(Regions.US_EAST_2)
                    .build();

            s3client.putObject(
                    bucketName,
                    name,
                    input
            );
        } catch (SdkClientException e) {
            e.printStackTrace();
        }


    }

    public String handleRequest(Map<String,Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("received request ");
        String mapToString = convetMapToJsonString(input);
        uploadToS3(S3_BUCKET_NAME,FILE_PREFIX+new Date().toString(),mapToString);
        return OK;
    }

    private String convetMapToJsonString(Map<String, Object> input) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();

        try {
             sb.append(objectMapper.writeValueAsString(input));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

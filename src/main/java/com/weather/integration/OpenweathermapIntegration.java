package com.weather.integration;

import com.weather.contracts.IGetWeatherData;
import com.weather.contracts.IWeatherDataParser;
import com.weather.models.WeatherData;
import com.weather.models.openweathermap.Sys;
import com.weather.models.openweathermap.WeatherWrapper;
import com.weather.parser.OpenweathermapParser;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.Base64;
/**
 * Created by pankaj on 3/15/20.
 */
public class OpenweathermapIntegration implements IGetWeatherData {

    IWeatherDataParser parser;
    private Client client;
    private WebTarget target;
    final static String OPENWEATHERMAPPROTOCOL = "http";
    final static String OPENWEATHERMAPHOST = "api.openweathermap.org";
    final static String OPENWEATHERBASEPATH = "data";
    final static String OPENWEATHERAPIVERSION = "2.5";
    final static String OPENWEATHERAPIENDPOINT = "weather";
    final static String PAGE_COUNT = "1";
    final static String API_MODE = "json";
    final static String UNIT_METRIC= "metric";

    final static Logger logger = Logger.getLogger(OpenweathermapIntegration.class);


    public void setParser(IWeatherDataParser parser) {
        this.parser = parser;
    }

    public WeatherData getData(String location) {
        WeatherData weatherData =null;
        if(parser!=null) {
            weatherData = parser.parse(getDataByLocation(location));
        }
        return weatherData;
    }

    public String getEndPoint() {
        String apiEndpoint = String.format("%s://%s/%s/%s/%s",OPENWEATHERMAPPROTOCOL,
                OPENWEATHERMAPHOST,
                OPENWEATHERBASEPATH,
                OPENWEATHERAPIVERSION,
                OPENWEATHERAPIENDPOINT);
        return apiEndpoint;
    }

    private Response getDataByLocation(String location) {

        Response weatherResponse=null;
        try {
            client = ClientBuilder.newClient();
            //example query params: ?q=Turku&cnt=10&mode=json&units=metric
            target = client.target(
                    getEndPoint()).queryParam("cnt", PAGE_COUNT)
                    .queryParam("mode", API_MODE)
                    .queryParam("units", UNIT_METRIC)
                    .queryParam("appid", decryptKey("APPID_ENC"))
            ;

             weatherResponse = target.queryParam("q", location)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class);

            logger.debug("Response code received from API "+weatherResponse.getStatus());

        } catch (Exception e) {
            logger.debug("Exception occurred when call the API");
            e.printStackTrace();
        }
        return weatherResponse;
    }

    private String decryptKey(String keyName) {
        logger.debug("Decrypting key");
        byte[] encryptedKey = Base64.decode(System.getenv(keyName));

        AWSKMS client = AWSKMSClientBuilder.defaultClient();

        DecryptRequest request = new DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(encryptedKey));

        ByteBuffer plainTextKey = client.decrypt(request).getPlaintext();
        return new String(plainTextKey.array(), Charset.forName("UTF-8"));
    }

    public static void main(String[] args) {
        OpenweathermapIntegration openweathermapIntegration = new OpenweathermapIntegration();
        openweathermapIntegration.setParser(new OpenweathermapParser());
        openweathermapIntegration.getData("Pune");
        UUID uuid = UUID.randomUUID();
        logger.debug("ID "+uuid.toString());

    }
}

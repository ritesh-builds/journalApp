package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import com.edigest.journalApp.cache.AppCache;
import com.edigest.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")  // $ add kiya
    private String apiKey;        // static hata diya

    @Autowired
    private AppCache appCache;
//    private static final String API = "https://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("Weather_of_" + city, WeatherResponse.class);
        if(weatherResponse != null){
            return weatherResponse;
        } else {
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY, city);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body != null){
                redisService.set("Weather_of_"+city, body, 300l);
            }
//            System.out.println("API KEY: " + apiKey);
//            System.out.println("FINAL API: " + finalAPI);
            return body;
        }

    }


}

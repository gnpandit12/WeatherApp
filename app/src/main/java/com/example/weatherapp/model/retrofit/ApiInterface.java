package com.example.weatherapp.model.retrofit;

import com.example.weatherapp.model.data.WeatherForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 20/05/24
 **/
public interface ApiInterface {

    @GET("/v1/forecast.json?")
    Call<WeatherForecastResponse> getWeatherForecast(
            @Query("key") String apikey,
            @Query("q") String location,
            @Query("days") String days,
            @Query("aqi") String airQualityIndex,
            @Query("alerts") String alerts
    );

}

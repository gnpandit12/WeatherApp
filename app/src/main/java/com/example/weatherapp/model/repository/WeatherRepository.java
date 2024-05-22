package com.example.weatherapp.model.repository;


import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.model.data.WeatherForecastResponse;
import com.example.weatherapp.model.retrofit.ApiInterface;
import com.example.weatherapp.model.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 20/05/24
 **/
public class WeatherRepository {
    private static ApiInterface apiInterface;
    private final MutableLiveData<WeatherForecastResponse> weatherForecastResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static WeatherRepository weatherRepository;
    public static WeatherRepository getInstance(){
        if (weatherRepository == null){
            weatherRepository = new WeatherRepository();
        }
        return weatherRepository;
    }

    public WeatherRepository(){
        apiInterface = RetrofitService.getInterface();
    }

    public MutableLiveData<WeatherForecastResponse> getWeatherForecast(
            String apiKey,
            String location,
            String days,
            String airQualityIndex,
            String alerts
    ) {
        isLoading.setValue(true);
        Call<WeatherForecastResponse> weatherForecastResponseCall = apiInterface.getWeatherForecast(
                apiKey,
                location,
                days,
                airQualityIndex,
                alerts
        );

        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                weatherForecastResponse.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable throwable) {
                weatherForecastResponse.setValue(null);
            }
        });
        return weatherForecastResponse;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

}

package com.example.weatherapp.viewModel;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.model.data.WeatherForecastResponse;
import com.example.weatherapp.model.repository.WeatherRepository;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class TodayWeatherViewModel extends AndroidViewModel {

    private WeatherRepository weatherRepository;
    private MutableLiveData<WeatherForecastResponse> weatherForecastResponse = new MutableLiveData<>();
    public TodayWeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository();
    }

    public MutableLiveData<WeatherForecastResponse> getMoviesRepository(
            String apiKey,
            String location,
            String days,
            String airQualityIndex,
            String alerts
    ) {
        weatherForecastResponse = getWeatherForecastResponse(
                apiKey,
                location,
                days,
                airQualityIndex,
                alerts
        );
        return weatherForecastResponse;
    }
    private MutableLiveData<WeatherForecastResponse> getWeatherForecastResponse(
            String apiKey,
            String location,
            String days,
            String airQualityIndex,
            String alerts
    ) {
        return weatherRepository.getWeatherForecast(
                apiKey,
                location,
                days,
                airQualityIndex,
                alerts
        );
    }

}

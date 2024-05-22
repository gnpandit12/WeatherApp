package com.example.weatherapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.weatherapp.databinding.FragmentTomorrowWeatherBinding;
import com.example.weatherapp.model.Constants;
import com.example.weatherapp.model.adapter.WeatherRecyclerViewAdapter;
import com.example.weatherapp.model.data.Hour;
import com.example.weatherapp.viewModel.WeatherForecastViewModel;

import java.util.ArrayList;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class TomorrowWeatherFragment extends Fragment implements MainActivity.CityNameListener {
    public static final String TAG = "TomorrowWeatherFragment";
    private FragmentTomorrowWeatherBinding fragmentTomorrowWeatherBinding;
    private WeatherForecastViewModel weatherForecastViewModel;
    private WeatherRecyclerViewAdapter weatherRecyclerViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).setCityNameListener(this);

        weatherForecastViewModel = new ViewModelProvider(requireActivity()).get(WeatherForecastViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTomorrowWeatherBinding = FragmentTomorrowWeatherBinding.inflate(inflater, container, false);
        return fragmentTomorrowWeatherBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherInfo(Constants.DEFAULT_CITY);
    }

    private void getWeatherInfo(String cityName) {
        weatherForecastViewModel.getWeatherForecastRepository(Constants.API_KEY, cityName, Constants.FORECAST_DAYS, Constants.AIR_QUALITY_INDEX, Constants.ALERTS)
                .observe(requireActivity(), weatherForecastResponse -> {
                    if (weatherForecastResponse != null) {
                        fragmentTomorrowWeatherBinding.tomorrowWeatherConditionTextView.setText(weatherForecastResponse.getForecast().getForecastday().get(1).getDay().getCondition().getText());

                        String imageUrl = "https:"+weatherForecastResponse.getForecast().getForecastday().get(1).getDay().getCondition().getIcon();
                        Glide
                                .with(this)
                                .load(imageUrl)
                                .into(fragmentTomorrowWeatherBinding.tomorrowWeatherConditionIcon);

                        weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(getContext(), (ArrayList<Hour>) weatherForecastResponse.getForecast().getForecastday().get(1).getHour());
                        fragmentTomorrowWeatherBinding.tomorrowHourlyWeatherRecyclerView.setAdapter(weatherRecyclerViewAdapter);
                        weatherRecyclerViewAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Response null");
                    }
                });
    }

    @Override
    public void onCityNameReceived(String cityName) {
        getWeatherInfo(cityName);
    }
}
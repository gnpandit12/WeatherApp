package com.example.weatherapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.weatherapp.databinding.FragmentTodayWeatherBinding;
import com.example.weatherapp.model.Constants;
import com.example.weatherapp.model.adapter.WeatherRecyclerViewAdapter;
import com.example.weatherapp.model.data.Hour;
import com.example.weatherapp.viewModel.WeatherForecastViewModel;

import java.util.ArrayList;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class TodayWeatherFragment extends Fragment implements MainActivity.CityNameListener {

    public static final String TAG = "TodayWeatherFragment";
    private FragmentTodayWeatherBinding fragmentTodayWeatherBinding;
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
        fragmentTodayWeatherBinding = FragmentTodayWeatherBinding.inflate(inflater,container,false);
        return fragmentTodayWeatherBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentTodayWeatherBinding.progressBar.setVisibility(View.VISIBLE);
        getWeatherInfo(Constants.DEFAULT_CITY);
    }

    @Override
    public void onCityNameReceived(String cityName) {
        getWeatherInfo(cityName);
    }

    private void getWeatherInfo(String cityName) {

        weatherForecastViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    fragmentTodayWeatherBinding.progressBar.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.feelsLikeText.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.currentTempeartureTextView.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.feelsLikeTextView.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.currentWeatherTextView.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.currentWeatherIcon.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.hourlyWeatherRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    fragmentTodayWeatherBinding.progressBar.setVisibility(View.INVISIBLE);
                    fragmentTodayWeatherBinding.feelsLikeText.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.currentTempeartureTextView.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.feelsLikeTextView.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.currentWeatherTextView.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.currentWeatherIcon.setVisibility(View.VISIBLE);
                    fragmentTodayWeatherBinding.hourlyWeatherRecyclerView.setVisibility(View.VISIBLE);

                }
            }
        });

        weatherForecastViewModel.getWeatherForecastRepository(Constants.API_KEY, cityName, Constants.FORECAST_DAYS, Constants.AIR_QUALITY_INDEX, Constants.ALERTS)
                .observe(requireActivity(), weatherForecastResponse -> {
                    if (weatherForecastResponse != null) {
                        fragmentTodayWeatherBinding.currentTempeartureTextView.setText(
                                weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getTempC().intValue() + " " + "℃");
                        fragmentTodayWeatherBinding.feelsLikeTextView.setText(
                                weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getFeelslikeC().intValue() + " " + "℃");
                        fragmentTodayWeatherBinding.currentWeatherTextView.setText(weatherForecastResponse.getCurrent().getCondition().getText());

                        String imageUrl = "https:"+weatherForecastResponse.getCurrent().getCondition().getIcon();
                        Glide
                                .with(this)
                                .load(imageUrl)
                                .into(fragmentTodayWeatherBinding.currentWeatherIcon);

                        weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(getContext(), (ArrayList<Hour>) weatherForecastResponse.getForecast().getForecastday().get(0).getHour());
                        fragmentTodayWeatherBinding.hourlyWeatherRecyclerView.setAdapter(weatherRecyclerViewAdapter);
                        weatherRecyclerViewAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Response null");
                    }
                });
    }

}
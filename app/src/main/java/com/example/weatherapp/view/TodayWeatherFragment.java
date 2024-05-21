package com.example.weatherapp.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentTodayWeatherBinding;
import com.example.weatherapp.model.Constants;
import com.example.weatherapp.model.data.WeatherForecastResponse;
import com.example.weatherapp.viewModel.TodayWeatherViewModel;

import java.util.Objects;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class TodayWeatherFragment extends Fragment implements MainActivity.CityNameListener{

    public static final String TAG = "TodayWeatherFragment";
    private FragmentTodayWeatherBinding fragmentTodayWeatherBinding;
    private TodayWeatherViewModel todayWeatherViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).setCityNameListener(this);

        todayWeatherViewModel  = new ViewModelProvider(requireActivity()).get(TodayWeatherViewModel.class);
        todayWeatherViewModel.getMoviesRepository(Constants.API_KEY, "Mumbai", "1", Constants.AIR_QUALITY_INDEX, Constants.ALERTS)
                .observe(requireActivity(), weatherForecastResponse -> {
                    if (weatherForecastResponse != null) {
                        fragmentTodayWeatherBinding.currentTempeartureTextView.setText(weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getTempC().toString());
                        fragmentTodayWeatherBinding.feelsLikeTextView.setText(weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getFeelslikeC().toString());
                        fragmentTodayWeatherBinding.currentWeatherTextView.setText(weatherForecastResponse.getCurrent().getCondition().getText());

                        String imageUrl = "https:"+weatherForecastResponse.getCurrent().getCondition().getIcon();
                        Glide
                                .with(this)
                                .load(imageUrl)
                                .into(fragmentTodayWeatherBinding.currentWeatherIcon);
                    } else {
                        Log.d(TAG, "Response null");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTodayWeatherBinding = FragmentTodayWeatherBinding.inflate(inflater,container,false);
        return fragmentTodayWeatherBinding.getRoot();
    }

    @Override
    public void onCityNameReceived(String cityName) {
        todayWeatherViewModel.getMoviesRepository(Constants.API_KEY, cityName, "1", Constants.AIR_QUALITY_INDEX, Constants.ALERTS)
                .observe(requireActivity(), weatherForecastResponse -> {
                    if (weatherForecastResponse != null) {
                        fragmentTodayWeatherBinding.currentTempeartureTextView.setText(weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getTempC().toString());
                        fragmentTodayWeatherBinding.feelsLikeTextView.setText(weatherForecastResponse.getForecast().getForecastday().get(0).getHour().get(0).getFeelslikeC().toString());
                        fragmentTodayWeatherBinding.currentWeatherTextView.setText(weatherForecastResponse.getCurrent().getCondition().getText());

                        String imageUrl = "https:"+weatherForecastResponse.getCurrent().getCondition().getIcon();
                        Glide
                                .with(this)
                                .load(imageUrl)
                                .into(fragmentTodayWeatherBinding.currentWeatherIcon);
                    } else {
                        Log.d(TAG, "Response null");
                    }
                });
    }
}
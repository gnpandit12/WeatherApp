package com.example.weatherapp.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentTodayWeatherBinding;
import com.example.weatherapp.model.Constants;
import com.example.weatherapp.model.adapter.WeatherRecyclerViewAdapter;
import com.example.weatherapp.model.data.Hour;
import com.example.weatherapp.model.data.WeatherForecastResponse;
import com.example.weatherapp.viewModel.TodayWeatherViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class TodayWeatherFragment extends Fragment implements MainActivity.CityNameListener{

    public static final String TAG = "TodayWeatherFragment";
    private FragmentTodayWeatherBinding fragmentTodayWeatherBinding;
    private TodayWeatherViewModel todayWeatherViewModel;
    private WeatherRecyclerViewAdapter weatherRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).setCityNameListener(this);

//        fragmentTodayWeatherBinding.hourlyWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        fragmentTodayWeatherBinding.hourlyWeatherRecyclerView.hasFixedSize();

        todayWeatherViewModel  = new ViewModelProvider(requireActivity()).get(TodayWeatherViewModel.class);

        getWeatherInfo(Constants.DEFAULT_CITY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTodayWeatherBinding = FragmentTodayWeatherBinding.inflate(inflater,container,false);
        return fragmentTodayWeatherBinding.getRoot();
    }

    @Override
    public void onCityNameReceived(String cityName) {
        getWeatherInfo(cityName);
    }

    private void getWeatherInfo(String cityName) {
        todayWeatherViewModel.getMoviesRepository(Constants.API_KEY, cityName, Constants.FORECAST_DAYS, Constants.AIR_QUALITY_INDEX, Constants.ALERTS)
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
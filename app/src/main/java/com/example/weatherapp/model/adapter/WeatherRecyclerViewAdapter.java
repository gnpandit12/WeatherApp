package com.example.weatherapp.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.model.data.Hour;

import java.util.ArrayList;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 21/05/24
 **/
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private Context mContext;
    private ArrayList<Hour> mHourlyWeatherArrayList;
    private String hourlyTime;
    public WeatherRecyclerViewAdapter(Context context, ArrayList<Hour> hourlyWeatherArrayList) {
        this.mContext = context;
        this.mHourlyWeatherArrayList = hourlyWeatherArrayList;
    }
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hourly_weather_card_view, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Hour hourlyWeather = mHourlyWeatherArrayList.get(position);

        String imageUrl = "https:"+hourlyWeather.getCondition().getIcon();
        Glide
                .with(mContext)
                .load(imageUrl)
                .into(holder.weatherImageView);
        holder.temperatureTextView.setText(
               hourlyWeather.getTempC().intValue() + " " + "℃"
        );


        if (position == 0) {
            hourlyTime = "12 am";
        } else if (position >0 && position < 12) {
            hourlyTime = position + " am";
        } else if (position == 12) {
            hourlyTime = position + " pm";
        } else if (position >=13 && position <= 23) {
            hourlyTime = position - 12 + " pm";
        }

        holder.timeTextView.setText(hourlyTime);

    }

    @Override
    public int getItemCount() {
        return mHourlyWeatherArrayList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherImageView;
        TextView timeTextView;
        TextView temperatureTextView;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherImageView = itemView.findViewById(R.id.hourly_weather_image_view);
            timeTextView = itemView.findViewById(R.id.hourly_weather_time_text_view);
            temperatureTextView = itemView.findViewById(R.id.hourly_weather_temperature_text_view);

        }
    }

}

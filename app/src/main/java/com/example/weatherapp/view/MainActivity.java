package com.example.weatherapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * @Author Gaurav Naresh Pandit
 * @Since 18/05/24
 **/
public class MainActivity extends AppCompatActivity {

    private final String[] tabLabels = new String[]{"Today", "Tomorrow"};
    public static final String TAG = "MainActivity";

    private CityNameListener cityNameListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        ViewPager2 viewPager2 = activityMainBinding.viewPager;
        viewPager2.setAdapter(new ViewPagerAdapter(this));

        TabLayout tabLayout = activityMainBinding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager2, (tab, i) -> tab.setText(tabLabels[i])).attach();

        activityMainBinding.cityNameEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, "IME_ACTION_DONE");
                if (activityMainBinding.cityNameEditText.getText() != null) {
                    Log.d(TAG, "City name not null");
                    cityNameListener.onCityNameReceived(activityMainBinding.cityNameEditText.getText().toString().trim());
                } else {
                    Log.d(TAG, "City name null!");
                }
            }
            return false;
        });

    }

    public interface CityNameListener {
        void onCityNameReceived(String cityName);
    }

    public void setCityNameListener(CityNameListener listener) {
        this.cityNameListener = listener;
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TodayWeatherFragment();
                case 1:
                    return new TomorrowWeatherFragment();
            }
            return new TodayWeatherFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
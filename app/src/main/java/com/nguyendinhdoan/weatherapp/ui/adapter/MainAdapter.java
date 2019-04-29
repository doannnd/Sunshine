package com.nguyendinhdoan.weatherapp.ui.adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nguyendinhdoan.weatherapp.R;
import com.nguyendinhdoan.weatherapp.ui.fragment.TodayWeatherFragment;

public class MainAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 1;

    private Context context;
    private Location currentLocation;

    public MainAdapter(Context context, Location currentLocation, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.currentLocation = currentLocation;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TodayWeatherFragment.newInstance(currentLocation);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.weather_today_fragment);
            default:
                return null;
        }

    }
}

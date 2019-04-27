package com.nguyendinhdoan.weatherapp.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nguyendinhdoan.weatherapp.R;
import com.nguyendinhdoan.weatherapp.ui.fragment.TodayWeatherFragment;

public class MainAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 1;

    private Context context;

    public MainAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return new TodayWeatherFragment();
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(R.string.weather_today_fragment);
    }
}

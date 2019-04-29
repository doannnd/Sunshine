package com.nguyendinhdoan.weatherapp.ui.fragment;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nguyendinhdoan.weatherapp.R;
import com.nguyendinhdoan.weatherapp.common.Common;
import com.nguyendinhdoan.weatherapp.data.model.WeatherForecastResult;
import com.nguyendinhdoan.weatherapp.data.remote.IOpenWeatherMap;
import com.nguyendinhdoan.weatherapp.ui.adapter.ForecastAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeatherFragment extends Fragment {

    private static final String TAG = "TODAY_WEATHER_FRAGMENT";
    private static final String LOCATION_LATITUDE = "LATITUDE";
    private static final String LOCATION_LONGITUDE = "LONGITUDE";
    @BindView(R.id.forecast_recycler_view)
    RecyclerView forecastRecyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar loadingProgressBar;
    Unbinder unbinder;

    private IOpenWeatherMap openWeatherMapService;
    private CompositeDisposable compositeDisposable;

    public static ForecastWeatherFragment newInstance(Location currentLocation) {
        ForecastWeatherFragment forecastWeatherFragment = new ForecastWeatherFragment();
        Bundle args = new Bundle();
        args.putDouble(LOCATION_LATITUDE, currentLocation.getLatitude());
        args.putDouble(LOCATION_LONGITUDE, currentLocation.getLongitude());
        forecastWeatherFragment.setArguments(args);
        return forecastWeatherFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);
        unbinder = ButterKnife.bind(this, view);

        Context activity = getActivity();
        openWeatherMapService = Common.getRetrofitClient();

        if (getArguments() != null && activity != null) {
            String latitude = String.valueOf(getArguments().getDouble(LOCATION_LATITUDE));
            String longitude = String.valueOf(getArguments().getDouble(LOCATION_LONGITUDE));
            String appId = Common.APP_ID;
            String unit = Common.UNITS;

            if (Common.isNetworkConnect(activity)) {
                loadWeatherForecastData(latitude, longitude, appId, unit);
            } else {
                Toast.makeText(activity, getString(R.string.request_internet), Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void loadWeatherForecastData(String latitude, String longitude, String appId, String unit) {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(openWeatherMapService.getWeatherForecastByLatLng(
                latitude, longitude, appId, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) {
                        displayWeatherForecast5Day(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, "error load weather data in 5 day" + throwable.getMessage());
                    }
                })
        );
    }

    private void displayWeatherForecast5Day(WeatherForecastResult weatherForecastResult) {
        forecastRecyclerView.setHasFixedSize(true);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ForecastAdapter forecastAdapter = new ForecastAdapter(weatherForecastResult, getActivity());
        forecastRecyclerView.setAdapter(forecastAdapter);
        // hide loading
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

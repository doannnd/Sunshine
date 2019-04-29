package com.nguyendinhdoan.weatherapp.ui.fragment;


import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyendinhdoan.weatherapp.R;
import com.nguyendinhdoan.weatherapp.common.Common;
import com.nguyendinhdoan.weatherapp.data.model.WeatherResult;
import com.nguyendinhdoan.weatherapp.data.remote.IOpenWeatherMap;
import com.squareup.picasso.Picasso;

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
public class TodayWeatherFragment extends Fragment {

    private static final String TAG = "TODAY WEATHER FRAGMENT";
    private static final String ICON_SCHEME = "https";
    private static final String ICON_AUTHORITY = "openweathermap.org";
    private static final String ICON_IMG_KEY = "img";
    private static final String ICON_W_KEY = "w";
    private static final String ICON_TYPE_KEY = ".png";
    private static final String LOCATION_LATITUDE = "LATITUDE";
    private static final String LOCATION_LONGITUDE = "LONGITUDE";

    @BindView(R.id.date_time_text_view)
    TextView dateTimeTextView;
    @BindView(R.id.city_text_view)
    TextView cityTextView;
    @BindView(R.id.temperature_text_view)
    TextView temperatureTextView;
    @BindView(R.id.icon_image_view)
    ImageView iconImageView;
    @BindView(R.id.description_text_view)
    TextView descriptionTextView;
    @BindView(R.id.wind_text_view)
    TextView windTextView;
    @BindView(R.id.pressure_text_view)
    TextView pressureTextView;
    @BindView(R.id.humidity_text_view)
    TextView humidityTextView;
    @BindView(R.id.sunrise_text_view)
    TextView sunriseTextView;
    @BindView(R.id.sunset_text_view)
    TextView sunsetTextView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar loadingProgressbar;
    Unbinder unbinder;

    private IOpenWeatherMap openWeatherMapService;
    private  CompositeDisposable compositeDisposable;

    public static TodayWeatherFragment newInstance(Location currentLocation) {
        TodayWeatherFragment todayWeatherFragment = new TodayWeatherFragment();
        Bundle args = new Bundle();
        args.putDouble(LOCATION_LATITUDE, currentLocation.getLatitude());
        args.putDouble(LOCATION_LONGITUDE, currentLocation.getLongitude());
        todayWeatherFragment.setArguments(args);
        return todayWeatherFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        unbinder = ButterKnife.bind(this, view);

        Context activity = getActivity();
        openWeatherMapService = Common.getRetrofitClient();

        if (getArguments() != null && activity != null) {
            String latitude = String.valueOf(getArguments().getDouble(LOCATION_LATITUDE));
            String longitude = String.valueOf(getArguments().getDouble(LOCATION_LONGITUDE));
            String appId = Common.APP_ID;
            String unit = Common.UNITS;

            if (Common.isNetworkConnect(activity)) {
                loadWeatherData(latitude, longitude, appId, unit);
            } else {
                Toast.makeText(activity, getString(R.string.request_internet), Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void loadWeatherData(String latitude, String longitude, String appId, String unit) {

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                openWeatherMapService
                        .getWeatherByLatLng(latitude, longitude, appId, unit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherResult>() {
                                       @Override
                                       public void accept(WeatherResult weatherResult) {
                                           updateUI(weatherResult);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) {
                                           Log.e(TAG, "Error load data from open weather map");
                                       }
                                   }
                        ));
    }

    private void updateUI(WeatherResult weatherResult) {
        // Load icon image
        String iconName = weatherResult.getWeather().get(0).getIcon();
        String iconPath =
                new Uri.Builder().scheme(ICON_SCHEME)
                        .authority(ICON_AUTHORITY)
                        .appendPath(ICON_IMG_KEY)
                        .appendPath(ICON_W_KEY)
                        .appendPath(iconName + ICON_TYPE_KEY)
                        .build().toString();
        Log.d(TAG, "icon url: " + iconPath);
        Picasso.get().load(iconPath).into(iconImageView);

        // load city name
        String cityName = weatherResult.getName();
        cityTextView.setText(cityName);

        // load temperature
        int temperature = (int) weatherResult.getMain().getTemp();
        temperatureTextView.setText(getString(R.string.temperature_value, temperature));

        // load description
        String description = weatherResult.getWeather().get(0).getDescription();
        descriptionTextView.setText(description);

        // load date time
        int dateTime = weatherResult.getDt();
        String timeFormatted = Common.convertUnixToDate(dateTime);
        dateTimeTextView.setText(timeFormatted);

        // load wind speed
        double windSpeed = weatherResult.getWind().getSpeed();
        windTextView.setText(getString(R.string.wind_value_text,
                String.valueOf(windSpeed)));

        // load pressure
        double pressure = weatherResult.getMain().getPressure();
        pressureTextView.setText(getString(R.string.pressure_value_text,
                String.valueOf(pressure)));

        // load humidity
        double humidity = weatherResult.getMain().getHumidity();
        humidityTextView.setText(getString(R.string.humidity_value_text,
                String.valueOf(humidity), "%"));

        // load sunset
        int sunset = weatherResult.getSys().getSunset();
        String sunsetFormatted = Common.convertUnixToHour(sunset);
        sunsetTextView.setText(getString(R.string.sunset_value_text,
                sunsetFormatted));

        // load sunrise
        int sunrise = weatherResult.getSys().getSunrise();
        String sunriseFormatted = Common.convertUnixToHour(sunrise);
        sunriseTextView.setText(getString(R.string.sunrise_value_text,
                sunriseFormatted));

        loadingProgressbar.setVisibility(View.GONE);
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

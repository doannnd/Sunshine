package com.nguyendinhdoan.weatherapp.common;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.nguyendinhdoan.weatherapp.data.remote.IOpenWeatherMap;
import com.nguyendinhdoan.weatherapp.data.remote.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Common {

    public static final String APP_ID = "7ec3e21f3bb9367814e264f0d2782e1d";
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/";
    public static final String UNITS = "metric";

    public static IOpenWeatherMap getRetrofitClient() {
        return RetrofitClient.getRetrofitClient(baseURL).create(IOpenWeatherMap.class);
    }

    public static String convertUnixToDate(int dateTime) {
        Date date = new Date(dateTime * 1000L);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String convertUnixToHour(int hour) {
        Date date = new Date(hour * 1000L);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static boolean isNetworkConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}

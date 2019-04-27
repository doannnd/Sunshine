package com.nguyendinhdoan.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {
    @Expose
    @SerializedName("sunset")
    private int sunset;
    @Expose
    @SerializedName("sunrise")
    private int sunrise;
    @Expose
    @SerializedName("country")
    private String country;
    @Expose
    @SerializedName("message")
    private double message;

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }
}

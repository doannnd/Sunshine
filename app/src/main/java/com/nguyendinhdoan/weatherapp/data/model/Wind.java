package com.nguyendinhdoan.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @Expose
    @SerializedName("deg")
    private double deg;
    @Expose
    @SerializedName("speed")
    private double speed;

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

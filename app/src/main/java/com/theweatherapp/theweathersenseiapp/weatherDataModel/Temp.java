package com.theweatherapp.theweathersenseiapp.weatherDataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temp implements Parcelable {

    @SerializedName("day")
    @Expose
    private double day;
    @SerializedName("min")
    @Expose
    private double min;
    @SerializedName("max")
    @Expose
    private double max;
    @SerializedName("night")
    @Expose
    private double night;
    @SerializedName("eve")
    @Expose
    private double eve;
    @SerializedName("morn")
    @Expose
    private double morn;

    /**
     * No args constructor for use in serialization
     *
     */
    public Temp() {
    }


    public Temp(double day, double min, double max, double night, double eve, double morn) {
        super();
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.day);
        dest.writeDouble(this.min);
        dest.writeDouble(this.max);
        dest.writeDouble(this.night);
        dest.writeDouble(this.eve);
        dest.writeDouble(this.morn);
    }

    protected Temp(Parcel in) {
        this.day = in.readDouble();
        this.min = in.readDouble();
        this.max = in.readDouble();
        this.night = in.readDouble();
        this.eve = in.readDouble();
        this.morn = in.readDouble();
    }

    public static final Creator<Temp> CREATOR = new Creator<Temp>() {
        @Override
        public Temp createFromParcel(Parcel source) {
            return new Temp(source);
        }

        @Override
        public Temp[] newArray(int size) {
            return new Temp[size];
        }
    };
}
